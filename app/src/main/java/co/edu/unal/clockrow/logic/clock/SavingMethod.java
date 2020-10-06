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
    }

    public SavingMethod(long workTime, long breakTime, long rewardTime, int numSessions) {
        super(workTime, breakTime, rewardTime);
        this.numSessions = numSessions;
        setRunning(false);
        sessionsCount = 0;
    }

    @Override
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
    void start(final TimeListener<String> listener) {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                listener.onTimerResponse(clockText(mTimeLeftInMillis));
            }

            @Override
            public void onFinish() {
                listener.onTimerFinish(clockText(mTimeLeftInMillis));
            }
        }.start();
        setRunning(true);
    }

    @Override
    void pause() {
        mCountDownTimer.cancel();
        setRunning(false);
    }

    @Override
    void reset() {

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
}
