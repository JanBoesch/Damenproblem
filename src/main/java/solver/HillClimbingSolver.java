package solver;

import obj.NQueens;

import java.util.Arrays;

public class HillClimbingSolver implements ISolver {

    @Override
    public NQueens solve(int boardSize) {
        return firstChoiceHillClimbing(boardSize);
    }

    // make a move for hill climbing
    public NQueens firstChoiceHillClimbing(int n) {
        NQueens nQueens = new NQueens(n);

        // terminate when it reaches max num of iterations or problem is solved.
        while (nQueens.getFitness() > 0) {

            boolean flag = true;
            int tempCostToBeat = nQueens.getFitness();

            for (int col = 0; col < n && flag; col++) {

                for (int row = 0; row < n; row++) {
                    // we do not need to evaluate because we already know current cost by costToBeat.
                    if (row == nQueens.getQueens()[col])
                        continue;

                    // init new copy
                    int[] randomStateCopy = Arrays.copyOf(nQueens.getQueens(), n);
                    randomStateCopy[col] = row;

                    NQueens tmpNQueens = new NQueens(n, randomStateCopy);

                    if (nQueens.getFitness() > tmpNQueens.getFitness()) {
                        nQueens = tmpNQueens;
                        flag = false;
                        break;
                    }
                }
            }

            // if it gets stuck at local maxima/minima
            if (tempCostToBeat == nQueens.getFitness()) {
                nQueens = new NQueens(n);
            }
        }

        return nQueens.getFitness() == 0 ? nQueens : null; // return solution if solved
    }

}
