package test;

import obj.NQueens;
import solver.ISolver;
import solver.SimulatedAnnealingSolver;

public class TestFrameSimAnnealing {

    public static void main(String[] args) {

        ISolver solver;
        int boardSize = 30;
        int iterations = 20;
        long average = 0;
        long currentBest = Long.MAX_VALUE;

        long timeStart;
        long timeEnd;

        int maxTemperature = 120;
        double macCoolingFact = 1;

        NQueens solution;

        System.out.println("Iterations: " + iterations);
        System.out.printf("%15s | %15s | %15s \n", "Temperature", "CoolingFactor", "Average");

        for (double c = 0.2; c <= macCoolingFact; c += 0.05) {

            for (int t = 10; t <= maxTemperature; t += 10) {

                for (int i = 1; i <= iterations; i++) {
                    timeStart = System.currentTimeMillis();

                    do {
                        solver = new SimulatedAnnealingSolver(t, c);
                        solution = solver.solve(boardSize);
                    } while (solution == null);

                    timeEnd = System.currentTimeMillis();
                    average += timeEnd - timeStart;
                }

                average = average / iterations;

                System.out.printf("%15d | %15.2f | %13d ms \n", t, c, average);

                if (currentBest > average) currentBest = average;
                average = 0;
            }

        }

        System.out.println("Best: " + currentBest + " ms");
    }
}
