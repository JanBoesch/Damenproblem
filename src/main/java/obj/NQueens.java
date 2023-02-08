package obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NQueens {

    private final int n;
    private final int[] queens;
    private int fitness;
    private static final Random random = new Random();

    public NQueens(int n, int[] queens) {
        this.n = n;
        this.queens = queens;
        this.fitness = calculateFitness(queens);
    }

    public NQueens(int n) {
        this.n = n;
        this.queens = generateRandomState(n);
        this.fitness = calculateFitness(queens);
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public boolean isGoalState() {
        return fitness == 0;
    }

    public int[] getQueens() {
        return queens;
    }

    public int[][] get2DQueens() {
        int[][] erg = new int[n][n];

        for (int i = 0; i < queens.length; i++) {
            for (int j = 0; j < queens.length; j++) {
                if (queens[j] == i) erg[i][j] = 1;
                if (queens[j] != i) erg[i][j] = 0;
            }
        }

        return erg;
    }

    // Generates random initial state
    public int[] generateRandomState(int n) {

        int[] initQueens = new int[n];

        for (int i = 0; i < n; i++) {
            initQueens[i] = (int) (Math.random() * n);
        }

        return initQueens;
    }

    public List<NQueens> generateSuccessors() {
        List<NQueens> successors = new ArrayList<>();

        // Iterate through each column
        for (int i = 0; i < n; i++) {
            // Check if the current column is occupied
            if (queens[i] != -1) {
                // If it is, try moving the queen to a different row of the same column
                for (int j = 0; j < n; j++) {
                    // Skip the current row of the queen
                    if (queens[i] == j) continue;

                    // Create a new state by copying the current state
                    NQueens successor = new NQueens(n, queens.clone());

                    // Move the queen to a different row of the same column
                    successor.queens[i] = j;

                    // Calculate the heuristic value for the new state
                    successor.setFitness(calculateFitness(successor.queens));

                    // Add the new state to the list of successors
                    successors.add(successor);
                }
            // If the current column is empty, try placing a queen there
            } else {
                // Try placing a queen in each row of the current column
                for (int j = 0; j < n; j++) {
                    // Create a new state by copying the current state
                    NQueens successor = new NQueens(n, queens.clone());

                    // Place a queen in the current row of the current column
                    successor.queens[i] = j;

                    // Calculate the heuristic value for the new state
                    successor.setFitness(calculateFitness(successor.queens));

                    // Add the new state to the list of successors
                    successors.add(successor);
                }
            }
        }
        return successors;
    }

    public int calculateFitness(int[] queens) {
        int fitness = 0;

        // Iterate through each queen
        for (int i = 0; i < n; i++) {
            int queenRow = queens[i];

            // Check if the queen threatens any other queens
            for (int j = i + 1; j < n; j++) {
                int otherQueenRow = queens[j];

                // If the queens are on the same row or on the same diagonal, increase the fitness value
                if (queenRow == otherQueenRow || Math.abs(queenRow - otherQueenRow) == j - i) {
                    fitness++;
                }
            }
        }

        return fitness;
    }

    public static int calculateFitness(int n, int[] queens) {
        int fitness = 0;

        // Iterate through each queen
        for (int i = 0; i < n; i++) {
            int queenRow = queens[i];

            // Check if the queen threatens any other queens
            for (int j = i + 1; j < n; j++) {
                int otherQueenRow = queens[j];

                // If the queens are on the same row or on the same diagonal, increase the fitness value
                if (queenRow == otherQueenRow || Math.abs(queenRow - otherQueenRow) == j - i) {
                    fitness++;
                }
            }
        }

        return fitness;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < queens.length; i++) {

            sb.append("+");
            for (int k = 0; k < queens.length; k++) {
                sb.append("-----+");
            }
            sb.append("\n");

            for (int j = 0; j < queens.length; j++) {
                if (queens[j] == i) sb.append("|  Q  ");
                if (queens[j] != i) sb.append("|     ");
                if (j == queens.length - 1) sb.append("|");
            }
            sb.append("\n");
        }

        sb.append("+");
        for (int k = 0; k < queens.length; k++) {
            sb.append("-----+");
        }
        sb.append("\n");

        return sb.toString();
    }

    public NQueens crossover(NQueens other) {
        int[] childQueens = new int[n];

        // Choose a random crossover point
        int crossoverPoint = random.nextInt(n);

        // Copy the first part of this chromosome to the child
        System.arraycopy(queens, 0, childQueens, 0, crossoverPoint);

        // Copy the second part of the other chromosome to the child
        System.arraycopy(other.queens, crossoverPoint, childQueens, crossoverPoint, n - crossoverPoint);

        return new NQueens(n, childQueens);
    }

    public void mutate() {
        // Choose a random mutation point
        int mutationPoint = random.nextInt(n);

        // Choose a random value for the mutation
        queens[mutationPoint] = random.nextInt(n);

        this.fitness = calculateFitness(queens);
    }
}
