package co.edu.unal.clockrow.viewmodel;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.lifecycle.ViewModel;

import co.edu.unal.clockrow.R;
import co.edu.unal.clockrow.logic.clock.Clock;
import co.edu.unal.clockrow.logic.clock.ClockStates;
import co.edu.unal.clockrow.logic.clock.Marathon;
import co.edu.unal.clockrow.logic.clock.Pomodoro;
import co.edu.unal.clockrow.logic.clock.SavingMethod;
import co.edu.unal.clockrow.logic.clock.TimeControlMethod;
import co.edu.unal.clockrow.logic.clock.TimeListener;

public class ClockViewModel extends ViewModel {
    private String currentTime;
    private String alternativeTime;
    private Pomodoro mPomodoro;
    private SavingMethod mSavingMethod;
    private Marathon mMarathon;
    private TimeControlMethod method = TimeControlMethod.POMODORO;


    private Context ctx;

    public String getCurrentTime() {
        return currentTime;
    }

    public TimeControlMethod getMethod() {
        return method;
    }

    public void setMethod(TimeControlMethod method) {

        this.method = method;
        switch (method) {
            case POMODORO:
                currentTime = Clock.clockText(mPomodoro.getWorkTime());
                break;
            case SAVING_METHOD:
                currentTime = Clock.clockText(mSavingMethod.getWorkTime());
                break;
            case MARATHON:
                currentTime = Clock.clockText(mMarathon.getWorkTimeLeftInMillisAux());
                break;
        }


    }

    private static final String TAG = "ClockViewModel";

    /*Constructor*/
    public ClockViewModel() {
        this.mPomodoro = new Pomodoro();
        this.mSavingMethod = new SavingMethod();
        this.mMarathon = new Marathon();
        switch (method) {
            case POMODORO:
                currentTime = Clock.clockText(mPomodoro.getWorkTime());
                break;
            case SAVING_METHOD:
                currentTime = Clock.clockText(mSavingMethod.getWorkTime());
                break;
            case MARATHON:
                currentTime = Clock.clockText(mMarathon.getWorkTimeLeftInMillisAux());
                break;
        }
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public String getAlternativeTime() {
        return alternativeTime;
    }

    public Pomodoro getPomodoro() {
        return mPomodoro;
    }

    public void start(final TimeListener<String> listener) {
        switch (method) {
            case POMODORO:
                startPomodoro(listener);
                break;
            case SAVING_METHOD:
                startSavingMethod(listener);
                break;
            case MARATHON:
                startMarathon(listener);
                break;
        }
    }

    private void startPomodoro(final TimeListener<String> listener) {
        mPomodoro.start(new TimeListener<String>() {
            @Override
            public void onTimerResponse(String response) {
                currentTime = response;
                listener.onTimerResponse(currentTime);
            }

            @Override
            public void onTimerFinish(String response) {
                currentTime = response;
                listener.onTimerFinish(currentTime);
                MediaPlayer mp = MediaPlayer.create(ctx, R.raw.end);
                mp.start();
            }
        });
    }

    private void startMarathon(final TimeListener<String> listener) {
        mMarathon.start(new TimeListener<String>() {
            @Override
            public void onTimerResponse(String response) {
                currentTime = response;
                listener.onTimerResponse(currentTime);
            }

            @Override
            public void onTimerFinish(String response) {
                currentTime = response;
                listener.onTimerFinish(currentTime);
                MediaPlayer mp = MediaPlayer.create(ctx, R.raw.end);
                mp.start();
            }
        });
    }

    private void startSavingMethod(final TimeListener<String> listener) {
        mSavingMethod.start(new TimeListener<String>() {
            @Override
            public void onTimerResponse(String response) {
                currentTime = response;
                listener.onTimerResponse(currentTime);
            }

            @Override
            public void onTimerFinish(String response) {
                currentTime = response;
                listener.onTimerFinish(currentTime);
                MediaPlayer mp = MediaPlayer.create(ctx, R.raw.end);
                mp.start();
            }
        });
    }

    public void pause() {
        switch (method) {
            case POMODORO:
                mPomodoro.pause();
                currentTime = Clock.clockText(mPomodoro.getTimeLeftInMillis());
                break;
            case SAVING_METHOD:
                mSavingMethod.pause();
                currentTime = Clock.clockText(mSavingMethod.getTimeLeftInMillis());
                break;
            case MARATHON:
                mMarathon.pause();

                if (mMarathon.getCurrentState() == ClockStates.WORK){
                    currentTime = Clock.clockText(mMarathon.getWorkTimeLeftInMillisAux());
                }else {
                    currentTime = Clock.clockText(mMarathon.getBreakTimeLeftInMillisAux());
                }
                break;
        }


    }

    public void reset() {
        switch (method) {
            case POMODORO:
                currentTime = Clock.clockText(mPomodoro.getTimeLeftInMillis());
                mPomodoro.reset();
                break;
            case SAVING_METHOD:
                mSavingMethod.reset();
                currentTime = Clock.clockText(mSavingMethod.getTimeLeftInMillis());
                break;
            case MARATHON:
                mMarathon.reset();
                currentTime = Clock.clockText(mMarathon.getWorkTimeLeftInMillisAux());
                break;
        }
    }

    public void shiftTime() {
        switch (method) {
            case SAVING_METHOD:
                mSavingMethod.shiftState();
                currentTime = Clock.clockText(mSavingMethod.getTimeLeftInMillis());
                break;
            case MARATHON:
                mMarathon.shiftState();
                currentTime = Clock.clockText(mMarathon.getTimeLeftInMillis());
                break;
        }
    }

}
