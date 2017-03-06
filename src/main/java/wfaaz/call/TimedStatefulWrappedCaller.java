package wfaaz.call;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class TimedStatefulWrappedCaller {
    private Caller caller;
    private Long offlineTime;
    private State state;

    public TimedStatefulWrappedCaller(Caller caller) {
        this.caller = caller;
        state = State.UNDEFINED;
    }

    public State getState() {
        return state;
    }

    public void setStateOnlineAndFlush() {
        this.state = State.ONLINE;
        flushOfflineTime();
    }

    public void setStateOfflineAndUpdateOfflineTImeIfEmpty(long offlineTime) {
        this.state = State.OFFLINE;
        setOfflineIfEmpty(offlineTime);
    }

    public Caller getCaller() {
        return caller;
    }

    public long getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineIfEmpty(long offlineTime) {
        if (this.offlineTime == null) {
            this.offlineTime = offlineTime;
        }
    }

    public void flushOfflineTime() {
        offlineTime = null;
    }

    public enum State {
        UNDEFINED,
        ONLINE,
        OFFLINE;
    }
}
