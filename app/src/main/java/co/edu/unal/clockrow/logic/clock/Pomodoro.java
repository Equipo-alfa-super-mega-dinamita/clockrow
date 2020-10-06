package co.edu.unal.clockrow.logic.clock;

import android.os.CountDownTimer;
import android.util.Log;

public class Pomodoro extends Clock {
    private int sessionsCount;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    private int numSessions = 4;
    private static final String TAG = "Pomodoro";


    public Pomodoro() {
        super(1500000, 300000, 900000);
        setRunning(false);
        sessionsCount = 0;
        setUptMillis();
    }

    public Pomodoro(long workTime, long breakTime, long rewardTime, int numSessions) {
        super(workTime, breakTime, rewardTime);
        this.numSessions = numSessions;
        setRunning(false);
        sessionsCount = 0;
        setUptMillis();
    }

    private void setUptMillis() {
        if (getCurrentState() == ClockStates.BREAK) {
            if (sessionsCount % numSessions == 0) {
                mTimeLeftInMillis = getRewardTime();
            } else {
                mTimeLeftInMillis = getBreakTime();
            }
        } else {
            mTimeLeftInMillis = getWorkTime();
        }
    }

    public long getTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public int getSessionsCount() {
        return sessionsCount;
    }

    @Override
    public void start(final TimeListener<String> listener) {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                listener.onTimerResponse(clockText(mTimeLeftInMillis));
            }

            @Override
            public void onFinish() {
                if (getCurrentState() == ClockStates.WORK) {
                    sessionsCount++;
                }
                shiftState();
                setUptMillis();
                setRunning(false);
                listener.onTimerFinish(clockText(mTimeLeftInMillis));
            }
        }.start();
        setRunning(true);
    }

    @Override
    public void pause() {
        mCountDownTimer.cancel();
        setRunning(false);
    }

    @Override
    public void reset() {
        mTimeLeftInMillis = getWorkTime();
    }
}
