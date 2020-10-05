package co.edu.unal.clockrow.logic.clock;

import java.util.Locale;

public abstract class Clock {
    private String name;
    private final long workTime;
    private final long breakTime;
    private final long rewardTime;
    private boolean isRunning;
    private ClockStates currentState = ClockStates.WORK;
    public static final int MILLIS = 1000;

    protected Clock(long workTime, long breakTime, long rewardTime) {
        this.workTime = workTime;
        this.breakTime = breakTime;
        this.rewardTime = rewardTime;
    }

    public String getName() {
        return name;
    }

    public ClockStates getCurrentState() {
        return currentState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWorkTime() {
        return workTime;
    }


    public long getBreakTime() {
        return breakTime;
    }


    public long getRewardTime() {
        return rewardTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }


    abstract void start(TimeListener<String> listener);

    abstract void pause();

    abstract void reset();

    public void shiftState() {
        currentState = (currentState == ClockStates.BREAK) ? ClockStates.WORK : ClockStates.BREAK;
    }

    public static String clockText(long timeInMillis) {
        int minutes = (int) (timeInMillis / 1000) / 60;
        int seconds = (int) (timeInMillis / 1000) % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
