package wfaaz.monitor;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface Job extends Runnable {
    long getPollTimeMs();
    void setPollTimeMs(long pollTimeMs);
}
