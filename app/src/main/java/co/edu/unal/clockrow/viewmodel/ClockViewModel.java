package co.edu.unal.clockrow.viewmodel;

import androidx.lifecycle.ViewModel;

import co.edu.unal.clockrow.logic.clock.Clock;
import co.edu.unal.clockrow.logic.clock.Pomodoro;
import co.edu.unal.clockrow.logic.clock.TimeControlMethod;
import co.edu.unal.clockrow.logic.clock.TimeListener;

public class ClockViewModel extends ViewModel {
    private String time;
    private Pomodoro mPomodoro;
    private TimeControlMethod method = TimeControlMethod.POMODORO;

    public String getTime() {
        return time;
    }

    public TimeControlMethod getMethod() {
        return method;
    }

    public void setMethod(TimeControlMethod method) {
        this.method = method;
    }

    public ClockViewModel() {
        this.mPomodoro = new Pomodoro();
        switch (method) {
            case POMODORO:
                time = Clock.clockText(mPomodoro.getWorkTime());
                break;
            case MARATHON:
                break;
            case SAVING_METHOD:
                break;
        }
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
                time = response;
                listener.onTimerResponse(time);
            }

            @Override
            public void onTimerFinish(String response) {
                time = response;
                listener.onTimerFinish(time);
            }
        });
    }

    private void startMarathon(final TimeListener<String> listener) {
        //TODO
    }

    private void startSavingMethod(final TimeListener<String> listener) {
        //TODO
    }

    public void pause() {
        mPomodoro.pause();
        time = Clock.clockText(mPomodoro.getTimeLeftInMillis());
    }

    public void reset() {
        mPomodoro.reset();
        time = Clock.clockText(mPomodoro.getTimeLeftInMillis());
    }

}
