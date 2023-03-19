package com.upb.st.rps3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private final double learningRate = 0.1;
    private final double discountFactor = 0.9;
    private int[][] mBoard = new int[3][3];

    private final int ROCK = 0;
    private final int PAPER = 1;
    private final int SCISSORS = 2;

    private int[][] rewards = {
            // player chooses rock
            {0, -1, 1},
            // player chooses paper
            {1, 0, -1},
            // player chooses scissors
            {-1, 1, 0}
    };

    private int getMaxQValueIndex(double[] qValuesForState) {
        int maxIndex = 0;

        for (int i = 1; i < qValuesForState.length; i++) {
            if (qValuesForState[i] > qValuesForState[maxIndex]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private Button rockButton;
    private Button paperButton;
    private Button scissorsButton;

    private TextView stateView;
    private TextView scoreView;
    private TextView actionView;

    private int playerScore;
    private int computerScore;
    private int currentState;
    private double[][] qValues = {
            // state 0
            {0, 0, 0},
            // state 1
            {0, 0, 0},
            // state 2
            {0, 0, 0}
    };
    private Random random;


    private double[] updateQValue(double[] currentStateQValues, int action, double reward, double[] nextStateQValues) {
        double[] updatedQValues = Arrays.copyOf(currentStateQValues, currentStateQValues.length);
        int maxQValueIndex = getMaxQValueIndex(nextStateQValues);
        double maxQValue = nextStateQValues[maxQValueIndex];
        double currentQValue = currentStateQValues[action];
        double updatedQValue = currentQValue + learningRate * (reward + discountFactor * maxQValue - currentQValue);
        updatedQValues[action] = updatedQValue;
        return updatedQValues;
    }
    private int getNextState(int currentState, int playerMove) {
        int nextState = -1;
        switch (currentState) {
            case ROCK:
                if (playerMove == ROCK) {
                    nextState = ROCK;
                } else if (playerMove == PAPER) {
                    nextState = PAPER;
                } else if (playerMove == SCISSORS) {
                    nextState = SCISSORS;
                }
                break;
            case PAPER:
                if (playerMove == ROCK) {
                    nextState = SCISSORS;
                } else if (playerMove == PAPER) {
                    nextState = PAPER;
                } else if (playerMove == SCISSORS) {
                    nextState = ROCK;
                }
                break;
            case SCISSORS:
                if (playerMove == ROCK) {
                    nextState = PAPER;
                } else if (playerMove == PAPER) {
                    nextState = ROCK;
                } else if (playerMove == SCISSORS) {
                    nextState = SCISSORS;
                }
                break;
        }
        return nextState;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        int playerMove = 0;
        int currentState = getIntent().getIntExtra("currentState", 0);
        int nextState = getNextState(currentState, playerMove);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rockButton = findViewById(R.id.rock_button);
        paperButton = findViewById(R.id.paper_button);
        scissorsButton = findViewById(R.id.scissors_button);

        stateView = findViewById(R.id.state);
        scoreView = findViewById(R.id.score);
        actionView = findViewById(R.id.action);

        playerScore = 0;
        computerScore = 0;
        currentState = ROCK;

        updateViews();

        random = new Random();

        rockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(ROCK);
            }
        });

        paperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(PAPER);
            }
        });

        scissorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(SCISSORS);
            }
        });
    }
    private void updateViews() {
        stateView.setText("State: " + currentState);
        scoreView.setText("Score: " + playerScore + " - " + computerScore);
    }

    private int chooseMove() {
        double[] qValuesForState = qValues[currentState];
        int action = getMaxQValueIndex(qValuesForState);
        return action;
    }

    private void play(int playerMove) {
        int nextState = getNextState(currentState, playerMove);
        int computerMove = chooseMove();
        int reward = rewards[playerMove][computerMove];
        playerScore += reward;
        computerScore -= reward;
        qValues[currentState] = updateQValue(qValues[currentState], playerMove, reward, qValues[nextState]);
        currentState = computerMove;

        updateViews();

        if (playerScore >= 10 || computerScore >= 10) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
