package com.sdcc.util;

public class TestTimeUtils {
    private long totalSelectionNodeTimeElapsed;
    private long totalAppExecutionTimeElapsed;

    public TestTimeUtils() {
        this.totalAppExecutionTimeElapsed = 0;
        this.totalAppExecutionTimeElapsed = 0;
    }

    public synchronized void addExecTime(long time) {
        this.totalAppExecutionTimeElapsed += time;
    }

    public synchronized void addSelectTime(long time) {
        this.totalSelectionNodeTimeElapsed += time;
    }

    public synchronized long getTotalSelectionNodeTimeElapsed() {
        return this.totalSelectionNodeTimeElapsed;
    }

    public synchronized long getTotalAppExecutionTimeElapsed() {
        return this.totalAppExecutionTimeElapsed;
    }

    public synchronized String getTotalStats() {
        return "\nTotal time elapsed for the node selection: " + this.totalSelectionNodeTimeElapsed + "ms\n" +
                "Total time elapsed for the apps execution: " + this.totalAppExecutionTimeElapsed + "ms\n";
    }
}
