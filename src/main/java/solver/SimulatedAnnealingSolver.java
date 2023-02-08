package solver;

import obj.NQueens;

public class SimulatedAnnealingSolver implements ISolver {

    private double temperature;
    private final double coolingFactor;

    public SimulatedAnnealingSolver(double temperature, double coolingFactor) {
        this.temperature = temperature;
        this.coolingFactor = coolingFactor;
    }

    @Override
    public NQueens solve(int boardSize) {
        return simulateAnnealing(boardSize);
    }

    public NQueens simulateAnnealing(int boardSize) {
        NQueens initialState = new NQueens(boardSize);

        int maxNumOfIterations = 50000;

        // terminate when it reaches max num of iterations or problem is solved.
        for (int x = 0; x < maxNumOfIterations && initialState.getFitness() > 0; x++) {
            initialState = makeMove(initialState);
            temperature = Math.max(temperature * coolingFactor, 0.01);
        }

        return initialState.getFitness() == 0 ? initialState : null; // return solution if solved
    }

    private NQueens makeMove(NQueens initialState) {
        int[] queenConfig = initialState.getQueens();
        int boardSize = queenConfig.length;
        int cost;
        int delta;
        double acceptProb;

        while (true) {
            int nCol = (int) (Math.random() * boardSize);
            int nRow = (int) (Math.random() * boardSize);
            int tmpRow = queenConfig[nCol];
            queenConfig[nCol] = nRow;

            cost = NQueens.calculateFitness(boardSize, queenConfig);

            if (cost < initialState.getFitness()) {
                return new NQueens(boardSize, queenConfig);
            }

            delta = initialState.getFitness() - cost;
            acceptProb = Math.min(1, Math.exp(delta / temperature));

            if (Math.random() < acceptProb) {
                return new NQueens(boardSize, queenConfig);
            }

            queenConfig[nCol] = tmpRow;
        }
    }
}
