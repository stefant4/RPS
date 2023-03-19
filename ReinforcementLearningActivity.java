package com.upb.st.rps3;
import java.util.Random;

public class ReinforcementLearningActivity {
    private final double alpha = 0.1;
    private final double gamma = 0.9;
    private final double explorationRate = 0.1;
    private final int numActions = 3;
    private final int numStates = 3;
    private double[][] qTable = new double[numStates][numActions];

    private Random random = new Random();

    public int chooseAction(int state) {
        int action;

        // Explore
        if (random.nextDouble() < explorationRate) {
            action = random.nextInt(numActions);
        }
        // Exploit
        else {
            action = getBestAction(state);
        }

        return action;
    }

    public void updateQ(int state, int action, int nextState, int reward) {
        double maxQNextState = getMaxQ(nextState);
        double currentQ = qTable[state][action];

        double updatedQ = currentQ + alpha * (reward + gamma * maxQNextState - currentQ);
        qTable[state][action] = updatedQ;
    }

    private double getMaxQ(int state) {
        double maxQ = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < numActions; i++) {
            if (qTable[state][i] > maxQ) {
                maxQ = qTable[state][i];
            }
        }

        return maxQ;
    }

    private int getMaxQValueIndex(int state) {
        int maxIndex = 0;
        double[] qValuesForState = qTable[state];
        double maxValue = qValuesForState[0];
        for (int i = 1; i < qValuesForState.length; i++) {
            if (qValuesForState[i] > maxValue) {
                maxValue = qValuesForState[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private int getBestAction(int state) {
        int bestAction = 0;
        double maxQ = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < numActions; i++) {
            if (qTable[state][i] > maxQ) {
                bestAction = i;
                maxQ = qTable[state][i];
            }
        }

        return bestAction;
    }
}
