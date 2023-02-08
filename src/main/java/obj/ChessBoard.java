package obj;

import java.util.concurrent.TimeUnit;

public enum ChessBoard {
    CB_8X8(8,92,12, 1000, 500, 120, 0.6, TimeUnit.MILLISECONDS, 100, 5),
    CB_9X9(9,352,46, 1100, 1000, 120, 0.6, TimeUnit.SECONDS, 2, 1),
    CB_10X10(10, 724, 92, 2000, 700, 120, 0.6, TimeUnit.SECONDS, 2, 1);

    private final int n;
    private final int all;
    private final int real;
    private final int populationSize;
    private final int numOfGenerations;
    private final double temperature;
    private final double coolingFactor;
    private final TimeUnit timeUnit;
    private final long timOutAll;
    private final long timeOutReal;


    ChessBoard(int n, int all, int real, int populationSize, int numOfGenerations, double temperature, double coolingFactor, TimeUnit timeUnit, long timOutAll, long timeOutReal) {
        this.n = n;
        this.all = all;
        this.real = real;
        this.populationSize = populationSize;
        this.numOfGenerations = numOfGenerations;
        this.temperature = temperature;
        this.coolingFactor = coolingFactor;
        this.timeUnit = timeUnit;
        this.timOutAll = timOutAll;
        this.timeOutReal = timeOutReal;
    }

    public int getN() {
        return n;
    }

    public int getAll() {
        return all;
    }

    public int getReal() {
        return real;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long getTimOutAll() {
        return timOutAll;
    }

    public long getTimeOutReal() {
        return timeOutReal;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getNumOfGenerations() {
        return numOfGenerations;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getCoolingFactor() {
        return coolingFactor;
    }
}
