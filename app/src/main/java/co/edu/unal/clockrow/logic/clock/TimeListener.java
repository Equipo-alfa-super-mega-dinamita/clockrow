package co.edu.unal.clockrow.logic.clock;

public interface TimeListener <T> {
    void onTimerResponse(T response);
    void onTimerFinish(T response);
}
