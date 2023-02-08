package test;

import obj.NQueens;
import solver.ISolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class SolverRunner extends Thread {

    private final int chessBoardSize;
    private final ISolver solver;
    public final ReentrantLock lock = new ReentrantLock();
    private final CountDownLatch countDownLatch;

    private final List<int[][]> allSolutions = new ArrayList<>();
    private final List<int[][]> realSolutions = new ArrayList<>();

    public SolverRunner(int chessBoardSize, ISolver solver, CountDownLatch countDownLatch) {
        this.chessBoardSize = chessBoardSize;
        this.solver = solver;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {
            countDownLatch.countDown();
            countDownLatch.await();

            while (!isInterrupted()) {
                NQueens queensSolution = solver.solve(chessBoardSize);

                if(queensSolution != null) {
                    int[][] solution2DQueens = queensSolution.get2DQueens();
                    findSolutionBoards(solution2DQueens);
                    findRealSolutions(solution2DQueens);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void findRealSolutions(int[][] queensSolution) {
        boolean exists = false;

        for (int[][] realSolution : realSolutions) {
            if (Arrays.deepEquals(realSolution, queensSolution)) {
                exists = true;      // Doppeltes Board
                break;
            }
            if (Arrays.deepEquals(realSolution, reflectArray(queensSolution))) {
                exists = true;      // Gespiegeltes Board
                break;
            }
            int[][] rotatedSolution = rotateArray(queensSolution);
            if (Arrays.deepEquals(realSolution, rotatedSolution)) {
                exists = true;      // Gedrehtes Board 90°
                break;
            }
            if (Arrays.deepEquals(realSolution, reflectArray(rotatedSolution))) {
                exists = true;      // Gespiegeltes Board vom Board 90°
                break;
            }
            rotatedSolution = rotateArray(rotatedSolution);
            if (Arrays.deepEquals(realSolution, rotatedSolution)) {
                exists = true;      // Gedrehtes Board 180°
                break;
            }
            if (Arrays.deepEquals(realSolution, reflectArray(rotatedSolution))) {
                exists = true;      // Gespiegeltes Board vom Board 180°
                break;
            }
            rotatedSolution = rotateArray(rotatedSolution);
            if (Arrays.deepEquals(realSolution, rotatedSolution)) {
                exists = true;      // Gedrehtes Board 270°
                break;
            }
            if (Arrays.deepEquals(realSolution, reflectArray(rotatedSolution))) {
                exists = true;      // Gespiegeltes Board vom Board 270°
                break;
            }
        }

        if (!exists) {
            lock.lock();
            realSolutions.add(queensSolution);
            lock.unlock();
        }
    }

    private int[][] rotateArray(int[][] board) {
        int[][] rotatedArray = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);

        for (int i = 0; i < rotatedArray.length; i++) {
            for (int j = i; j < rotatedArray.length; j++) {
                int temp = rotatedArray[i][j];
                rotatedArray[i][j] = rotatedArray[j][i];
                rotatedArray[j][i] = temp;
            }
        }

        //then we reverse the elements of each row
        for (int i = 0; i < rotatedArray.length; i++) {
            //logic to reverse each row i.e 1D Array.
            int low = 0, high = rotatedArray.length - 1;
            while (low < high) {
                int temp = rotatedArray[i][low];
                rotatedArray[i][low] = rotatedArray[i][high];
                rotatedArray[i][high] = temp;
                low++;
                high--;
            }
        }

        return rotatedArray;
    }

    private void findSolutionBoards(int[][] queensSolution) {

        boolean exists = false;

        for (int[][] allSolution : allSolutions) {
            if (Arrays.deepEquals(allSolution, queensSolution)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            lock.lock();
            allSolutions.add(queensSolution);
            lock.unlock();
        }

    }

    private int[][] reflectArray(int[][] board) {
        int[][] reflectBoard = new int[board.length][board.length];

        for (int i = 0; i < board.length; i++) {
            int col = 0;
            for (int j = board.length - 1; j >= 0; j--) {
                reflectBoard[i][col] = board[i][j];
                col++;
            }
        }

        return reflectBoard;
    }

    public int getRealSolutions() {
        return realSolutions.size();
    }

    public int getAllSolutions() {
        return allSolutions.size();
    }
}
