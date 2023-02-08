package test;

import obj.NQueens;
import solver.GeneticAlgorithmSolver;
import solver.ISolver;

import java.io.*;

public class TestFrameGeneticAlgorithm {

    public static void main(String[] args) {

        ISolver solver;
        int boardSize = 10;
        int iterations = 10;
        long average = 0;
        long best = Long.MAX_VALUE;

        long timeStart;
        long time;

        int maxPopulationSize = 3000;
        int maxNumOfGenerations = 2000;

        NQueens solution;

        try {

            File dataFile = new File("src/main/resourcen/GenAlgTest.txt");
            dataFile.createNewFile();

            BufferedWriter out = new BufferedWriter(new FileWriter(dataFile.getAbsolutePath()));
            PrintWriter fout = new PrintWriter(out);

            System.out.println("Iterations: " + iterations);
            fout.write("Iterations: " + iterations + " \n");
            System.out.printf("%20s | %20s | %10s | %10s | %10s | %10s \n", "populationSize", "numOfGenerations", "Max", "Min", "Average", "Rounds");
            fout.printf("%20s | %20s | %10s | %10s | %10s | %10s \n", "populationSize", "numOfGenerations", "Max", "Min", "Average", "Rounds");

            for (int p = 1500; p <= maxPopulationSize; p += 100) {
                for (int g = 1000; g <= maxNumOfGenerations; g += 100) {

                    int rounds = 0;
                    long max = 0;
                    long min = 99999;
                    for (int i = 1; i <= iterations; i++) {
                        timeStart = System.currentTimeMillis();

                        do {
                            solver = new GeneticAlgorithmSolver(p, g);
                            solution = solver.solve(boardSize);
                            rounds++;
                        } while (solution == null);

                        time = System.currentTimeMillis() - timeStart;
                        average += time;
                        if(max < time) max = time;
                        if(min > time) min = time;
                    }

                    average = average / iterations;

                    System.out.printf("%20d | %20d | %7d ms | %7d ms | %7d ms | %10d \n", p, g, max, min, average, rounds);
                    fout.printf("%20d | %20d | %7d ms | %7d ms | %7d ms | %10d \n", p, g, max, min, average, rounds);
                    if (best > average) best = average;
                    average = 0;
                }
            }

            System.out.println("Best: " + best + " ms");
            fout.write("Best: " + best + " ms");

            fout.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
