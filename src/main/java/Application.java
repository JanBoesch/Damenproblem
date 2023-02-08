import obj.ChessBoard;
import obj.NQueens;
import solver.*;

public class Application {

    public static void main(String[] args) {

        ISolver solver;
        NQueens solution;

        ChessBoard cb  = ChessBoard.CB_10X10;

        System.out.println("HillClimbingSolver");
        int countHCRounds = 0;
        final long hillStart = System.currentTimeMillis();
        do {
            solver = new HillClimbingSolver();
            solution = solver.solve(cb.getN());
            countHCRounds++;
        } while (solution == null);
        final long hillEnd = System.currentTimeMillis();
        System.out.println(solver.solve(cb.getN()));
        System.out.println();

        System.out.println("SimulatedAnnealingSolver");
        int countSARounds = 0;
        final long simulatedStart = System.currentTimeMillis();
        do {
            solver = new SimulatedAnnealingSolver(cb.getTemperature(),cb.getCoolingFactor());
            solution = solver.solve(cb.getN());
            countSARounds++;
        } while (solution == null);
        final long simulatedEnd = System.currentTimeMillis();

        System.out.println(solver.solve(cb.getN()));
        System.out.println();

        System.out.println("GeneticAlgorithmSolver");
        int countGARounds = 0;

        final long geneticStart = System.currentTimeMillis();
        do {
            solver = new GeneticAlgorithmSolver(cb.getPopulationSize(), cb.getNumOfGenerations());
            solution = solver.solve(cb.getN());
            countGARounds++;
        } while (solution == null);
        final long geneticEnd = System.currentTimeMillis();

        System.out.println(solution);
        System.out.println();

        System.out.printf("%18s: %5d ms nach %3d Runden \n", "HillClimbing", hillEnd - hillStart, countHCRounds);
        System.out.printf("%18s: %5d ms nach %3d Runden \n", "SimulatedAnnealing", simulatedEnd - simulatedStart, countSARounds);
        System.out.printf("%18s: %5d ms nach %3d Runden \n", "GeneticAlgorithm", geneticEnd - geneticStart, countGARounds);
    }
}
