package wfaaz.monitor;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface Job {
    long getPollTimeSec();
    void setPollTimeSec(long pollTimeSec);
    void setOutageStatus(boolean out);
}
