package co.edu.unal.clockrow.logic.clock;

import android.os.CountDownTimer;

public class Marathon extends Clock {
    private long mBreakTimeLeftInMillisAux = 0;
    private long mWorkTimeLeftInMillisAux = 0;
    private long mWorkTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;
    private boolean isObligatoryBreak;

    public Marathon() {
        super(7200000, 300000, 900000);
        setRunning(false);
        setUpMillis();
    }

    public Marathon(long workTime, long breakTime, long rewardTime) {
        super(workTime, breakTime, rewardTime);
        setRunning(false);
        setUpMillis();
    }

    public long getWorkTimeLeftInMillisAux() {
        return mWorkTimeLeftInMillisAux;
    }

    public long getBreakTimeLeftInMillisAux() {
        return mBreakTimeLeftInMillisAux;
    }

    public long getTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }


    @Override
    public void start(TimeListener<String> listener) {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                if (getCurrentState() == ClockStates.WORK) {
                    mWorkTimeLeftInMillisAux += MILLIS;
                    mBreakTimeLeftInMillisAux += MILLIS / 4;
                    listener.onTimerResponse(Clock.clockText(mWorkTimeLeftInMillisAux));
                } else {
                    listener.onTimerResponse(Clock.clockText(mTimeLeftInMillis));
                }
            }

            @Override
            public void onFinish() {
                if (getCurrentState() == ClockStates.WORK) {
                    //comes for work and exceeds 2h
                    isObligatoryBreak = true;
                    mBreakTimeLeftInMillisAux += getRewardTime();
                    shiftState();
                    listener.onTimerFinish(Clock.clockText(mTimeLeftInMillis));
                } else {
                    //comes from a break
                    if (isObligatoryBreak) {
                        //comes from a ob work
                        isObligatoryBreak = false;
                        setUpMillis();
                    }
                    shiftState();
                    listener.onTimerFinish(Clock.clockText(mWorkTimeLeftInMillisAux));
                }

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
        setUpMillis();
        mBreakTimeLeftInMillisAux = 0;
        mWorkTimeLeftInMillisAux = 0;
    }

    public void shiftState() {
        super.shiftState();
        if (getCurrentState() == ClockStates.BREAK) {
            //going to rest
            if (isObligatoryBreak) {
                //it's a ob rest
                mTimeLeftInMillis = getBreakTime();
            } else {
                //it's a voluntary break
                mWorkTimeLeftInMillis = mTimeLeftInMillis;
                mTimeLeftInMillis = mBreakTimeLeftInMillisAux;
            }
        } else {
            //going to work
            mBreakTimeLeftInMillisAux = mTimeLeftInMillis;
            mTimeLeftInMillis = mWorkTimeLeftInMillis;
        }
    }

    private void setUpMillis() {
        mWorkTimeLeftInMillis = getWorkTime();
        mTimeLeftInMillis = mWorkTimeLeftInMillis;
    }

}
