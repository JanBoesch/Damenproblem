package test;


import obj.ChessBoard;
import solver.GeneticAlgorithmSolver;
import solver.HillClimbingSolver;
import solver.SimulatedAnnealingSolver;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestFramePerformance {

    private final ChessBoard cb = ChessBoard.CB_10X10;

    private SolverRunner hillClimbingRunner;
    private SolverRunner simulatedAnnealingRunner;
    private SolverRunner geneticAlgorithmRunner;

    private long timerStart = 0;
    private long timerEndHillClimbing = 0;
    private long timerEndSimulatedAnnealing = 0;
    private long timerEndGeneticAlgorithm = 0;

    public static void main(String[] args) {

        TestFramePerformance testFrame = new TestFramePerformance();
        File dataFile;
        BufferedWriter out;
        PrintWriter fOut;

        try {

            /* Alle Lösungen finden */
            dataFile = new File("src/main/resourcen/PerformanceAll_" + testFrame.cb.getN() + "x" + testFrame.cb.getN() + ".txt");
            dataFile.createNewFile();

            out = new BufferedWriter(new FileWriter(dataFile.getAbsolutePath()));
            fOut = new PrintWriter(out);

            System.out.println("Alle Lösungen (N = " + testFrame.cb.getN() + "): ");
            testFrame.findAllSolutions(fOut, testFrame.cb.getTimeUnit(), testFrame.cb.getTimOutAll());

            fOut.close();
            out.close();

            System.out.println();

            /* Alle realen Lösungen finden */
            dataFile = new File("src/main/resourcen/PerformanceReal_" + testFrame.cb.getN() + "x" + testFrame.cb.getN() + ".txt");
            dataFile.createNewFile();

            out = new BufferedWriter(new FileWriter(dataFile.getAbsolutePath()));
            fOut = new PrintWriter(out);

            System.out.println("Alle realen Lösungen (N = " + testFrame.cb.getN() + "): ");
            testFrame.findRealSolutions(fOut, testFrame.cb.getTimeUnit(), testFrame.cb.getTimeOutReal());

            fOut.close();
            out.close();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    public void findAllSolutions(PrintWriter fOut, TimeUnit timeUnit, long timeout) throws InterruptedException {

        long interval = 0;

        setUp(fOut, timeUnit);

        lock();

        do {
            fOut.printf("%20d | %18d | %18d | %18d | \n", interval, hillClimbingRunner.getAllSolutions(),
                    simulatedAnnealingRunner.getAllSolutions(), geneticAlgorithmRunner.getAllSolutions());

            unlock();
            timeUnit.sleep(timeout);
            interval += timeout;
            lock();

        } while (!checkAllFinish());

        unlock();

        fOut.printf("%20d | %18d | %18d | %18d | \n", interval, hillClimbingRunner.getAllSolutions(),
                simulatedAnnealingRunner.getAllSolutions(), geneticAlgorithmRunner.getAllSolutions());

        printFinalTime(timeUnit);
    }

    private void setUp(PrintWriter fOut, TimeUnit timeUnit) {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        hillClimbingRunner = new SolverRunner(cb.getN(), new HillClimbingSolver(), countDownLatch);
        simulatedAnnealingRunner = new SolverRunner(cb.getN(), new SimulatedAnnealingSolver(cb.getTemperature(), cb.getCoolingFactor()), countDownLatch);
        geneticAlgorithmRunner = new SolverRunner(cb.getN(), new GeneticAlgorithmSolver(cb.getPopulationSize(), cb.getNumOfGenerations()), countDownLatch);

        hillClimbingRunner.setName("HillClimbingRunner");
        simulatedAnnealingRunner.setName("SimulatedAnnealingRunner");
        geneticAlgorithmRunner.setName("GeneticAlgorithmRunner");

        fOut.printf("%20s | %18s | %18s | %18s | \n", "Time ("+ timeUnit.name()+")", "HillClimbing", "SimulatedAnnealing", "GeneticAlgorithm");

        timerStart = System.currentTimeMillis();

        hillClimbingRunner.start();
        simulatedAnnealingRunner.start();
        geneticAlgorithmRunner.start();
    }

    private void printFinalTime(TimeUnit timeUnit) {
        System.out.println();
        System.out.printf("%18s: %5d %s \n", "HillClimbing", cb.getTimeUnit().convert(timerEndHillClimbing - timerStart, TimeUnit.MILLISECONDS), timeUnit.name());
        System.out.printf("%18s: %5d %s \n", "SimulatedAnnealing", cb.getTimeUnit().convert(timerEndSimulatedAnnealing - timerStart, TimeUnit.MILLISECONDS), timeUnit.name());
        System.out.printf("%18s: %5d %s \n", "GeneticAlgorithm", cb.getTimeUnit().convert(timerEndGeneticAlgorithm - timerStart, TimeUnit.MILLISECONDS), timeUnit.name());
    }

    private void lock() {
        if(hillClimbingRunner.isAlive() && !hillClimbingRunner.isInterrupted()) hillClimbingRunner.lock.lock();
        if(simulatedAnnealingRunner.isAlive() && !simulatedAnnealingRunner.isInterrupted()) simulatedAnnealingRunner.lock.lock();
        if(geneticAlgorithmRunner.isAlive() && !geneticAlgorithmRunner.isInterrupted()) geneticAlgorithmRunner.lock.lock();
    }

    private void unlock() {
        if(hillClimbingRunner.isAlive() && !hillClimbingRunner.isInterrupted()) hillClimbingRunner.lock.unlock();
        if(simulatedAnnealingRunner.isAlive() && !simulatedAnnealingRunner.isInterrupted()) simulatedAnnealingRunner.lock.unlock();
        if(geneticAlgorithmRunner.isAlive() && !geneticAlgorithmRunner.isInterrupted()) geneticAlgorithmRunner.lock.unlock();
    }

    private void findRealSolutions(PrintWriter fOut, TimeUnit timeUnit, long timeout) throws InterruptedException {

        long interval = 0;

        setUp(fOut, timeUnit);

        lock();

        do {
            fOut.printf("%20d | %18d | %18d | %18d | \n", interval, hillClimbingRunner.getRealSolutions(),
                    simulatedAnnealingRunner.getRealSolutions(), geneticAlgorithmRunner.getRealSolutions());

            unlock();
            timeUnit.sleep(timeout);
            interval += timeout;
            lock();

        } while (!checkRealFinish());

        unlock();

        fOut.printf("%20d | %18d | %18d | %18d | \n", interval, hillClimbingRunner.getRealSolutions(),
                simulatedAnnealingRunner.getRealSolutions(), geneticAlgorithmRunner.getRealSolutions());

        printFinalTime(timeUnit);
    }

    private boolean checkAllFinish() {
        boolean finished = true;

        if (hillClimbingRunner.getAllSolutions() == cb.getAll() ) {
            if(!hillClimbingRunner.isInterrupted() && hillClimbingRunner.isAlive()) {
                timerEndHillClimbing = System.currentTimeMillis();
                hillClimbingRunner.interrupt();
            }
        } else {
            finished = false;
        }

        if (simulatedAnnealingRunner.getAllSolutions() == cb.getAll() ) {
            if(!simulatedAnnealingRunner.isInterrupted() && simulatedAnnealingRunner.isAlive()) {
                timerEndSimulatedAnnealing = System.currentTimeMillis();
                simulatedAnnealingRunner.interrupt();
            }
        } else {
            finished = false;
        }

        if (geneticAlgorithmRunner.getAllSolutions() == cb.getAll() ) {
            if(!geneticAlgorithmRunner.isInterrupted() && geneticAlgorithmRunner.isAlive()) {
                timerEndGeneticAlgorithm = System.currentTimeMillis();
                geneticAlgorithmRunner.interrupt();
            }
        } else {
            finished = false;
        }

        return finished;
    }

    private boolean checkRealFinish() {
        boolean finished = true;

        if (hillClimbingRunner.getRealSolutions() == cb.getReal() ) {
            if(!hillClimbingRunner.isInterrupted() && hillClimbingRunner.isAlive()) {
                timerEndHillClimbing = System.currentTimeMillis();
                hillClimbingRunner.interrupt();
            }
        } else {
            finished = false;
        }

        if (simulatedAnnealingRunner.getRealSolutions() == cb.getReal() ) {
            if(!simulatedAnnealingRunner.isInterrupted() && simulatedAnnealingRunner.isAlive()) {
                timerEndSimulatedAnnealing = System.currentTimeMillis();
                simulatedAnnealingRunner.interrupt();
            }
        } else {
            finished = false;
        }

        if (geneticAlgorithmRunner.getRealSolutions() == cb.getReal() ) {
            if(!geneticAlgorithmRunner.isInterrupted() && geneticAlgorithmRunner.isAlive()) {
                timerEndGeneticAlgorithm = System.currentTimeMillis();
                geneticAlgorithmRunner.interrupt();
            }
        } else {
            finished = false;
        }

        return finished;
    }
}
