package wfaaz.monitor.ping;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class PingResultImpl implements PingResult {
    private boolean onService;
    private long processingTimeMs;

    public PingResultImpl(boolean onService, long processingTimeMs) {
        this.onService = onService;
        this.processingTimeMs = processingTimeMs;
    }

    public boolean isOnService() {
        return onService;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    @Override
    public String toString() {
        return "PingResultImpl{" +
                "onService=" + onService +
                ", processingTimeMs=" + processingTimeMs +
                '}';
    }
}
