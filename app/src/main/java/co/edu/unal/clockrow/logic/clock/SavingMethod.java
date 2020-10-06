package co.edu.unal.clockrow.logic.clock;

import android.os.CountDownTimer;

public class SavingMethod extends Clock {
    private int sessionsCount;
    private long mBreakTimeLeftInMillis;
    private long mWorkTimeLeftInMillis;
    private int numSessions = 2;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;
    private static final String TAG = "SavingMethod";

    public SavingMethod() {
        super(3000000, 600000, 900000);
        setRunning(false);
        sessionsCount = 0;
        mWorkTimeLeftInMillis = getWorkTime();
        mBreakTimeLeftInMillis = getBreakTime();
        setUptMillis();
    }

    public SavingMethod(long workTime, long breakTime, long rewardTime, int numSessions) {
        super(workTime, breakTime, rewardTime);
        this.numSessions = numSessions;
        setRunning(false);
        sessionsCount = 0;
        mWorkTimeLeftInMillis = getWorkTime();
        mBreakTimeLeftInMillis = getBreakTime();
        setUptMillis();
    }

    public void shiftState() {
        super.shiftState();
        if (getCurrentState() == ClockStates.BREAK) {
            mWorkTimeLeftInMillis = mTimeLeftInMillis;
            mTimeLeftInMillis = mBreakTimeLeftInMillis;
        } else {
            mBreakTimeLeftInMillis = mTimeLeftInMillis;
            mTimeLeftInMillis = mWorkTimeLeftInMillis;
        }
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
        mWorkTimeLeftInMillis = getWorkTime();
        mTimeLeftInMillis = mWorkTimeLeftInMillis;
    }

    public long getBreakTimeLeftInMillis() {
        return mBreakTimeLeftInMillis;
    }

    public long getWorkTimeLeftInMillis() {
        return mWorkTimeLeftInMillis;
    }


    private void setUptMillis() {
        if (mBreakTimeLeftInMillis == 0 && mWorkTimeLeftInMillis == 0) {
            // fill up  time counters
            mWorkTimeLeftInMillis = getWorkTime();
            sessionsCount++;
            if (sessionsCount % numSessions == 0) {
                mBreakTimeLeftInMillis = getRewardTime();
            } else {
                mBreakTimeLeftInMillis = getBreakTime();
            }
        }
        //get value to  timeLeft
        if (getCurrentState() == ClockStates.BREAK) {
            mTimeLeftInMillis = mBreakTimeLeftInMillis;
        } else {
            mTimeLeftInMillis = mWorkTimeLeftInMillis;
        }
    }

    public int getSessionsCount() {
        return sessionsCount;
    }

    public long getTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }
}
