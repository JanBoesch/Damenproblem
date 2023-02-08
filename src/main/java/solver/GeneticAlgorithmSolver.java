package solver;

import obj.NQueens;

import java.util.*;

public class GeneticAlgorithmSolver implements ISolver {

    private final int populationSize;
    private final int numOfGenerations;
    private static Random random = new Random();

    public GeneticAlgorithmSolver(int populationSize, int numOfGenerations) {
        this.populationSize = populationSize;
        this.numOfGenerations = numOfGenerations;
    }

    @Override
    public NQueens solve(int boardSize) {
        return geneticAlgorithm(boardSize);
    }

    public NQueens geneticAlgorithm(int boardSize) {

        // Create the initial population
        List<NQueens> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new NQueens(boardSize, generateRandomQueens(boardSize)));
        }

        // Set the termination criteria
        int generation = 0;

        // Run the genetic algorithm until the termination criteria is met
        while (generation < numOfGenerations) {
            // Sort the population by heuristic value
            population.sort((s1, s2) -> s1.getFitness() - s2.getFitness());

            // Check if the goal state has been reached
            if (population.get(0).isGoalState()) {
                return population.get(0);
            }

            // Keep the top 50% of the population
            int newPopulationSize = populationSize / 2;
            List<NQueens> newPopulation = population.subList(0, newPopulationSize);

            // Generate offspring for the new population
            while (newPopulation.size() < populationSize) {
                // Choose two parents randomly
                NQueens parent1 = population.get(random.nextInt(newPopulationSize));
                NQueens parent2 = population.get(random.nextInt(newPopulationSize));

                // Generate an offspring using crossover
                NQueens offspring = parent1.crossover(parent2);

                // Mutate the offspring
                offspring.mutate();

                // Add the offspring to the new population
                newPopulation.add(offspring);
            }

            // Replace the old population with the new population
            population = newPopulation;

            // Increase the generation counter
            generation++;
        }

        return null;
    }

    private static int[] generateRandomQueens(int n) {
        int[] queens = new int[n];

        for (int i = 0; i < n; i++) {
            queens[i] = random.nextInt(n);
        }

        return queens;
    }
}
