package wfaaz.monitor.ping;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface PingResult {
    boolean isOnService();
    long getProcessingTimeMs();
}
