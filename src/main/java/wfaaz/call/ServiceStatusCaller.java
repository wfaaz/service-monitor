package wfaaz.call;

import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceStatusCaller implements Caller {
    private Logger log = Logger.getLogger(ServiceStatusCaller.class);

    private InetSocketAddress address;
    private long pollTimeMs;
    private long graceTimeMs;

    public ServiceStatusCaller(InetSocketAddress address, long pollTimeMs, long graceTimeMs) {
        this.address = address;
        if (graceTimeMs < pollTimeMs) {
            this.pollTimeMs = graceTimeMs;
        } else {
            this.pollTimeMs = pollTimeMs;
        }
        this.graceTimeMs = graceTimeMs;
    }

    public void doNotify(boolean onService) {
        log.info("Service:" + address + " status=" + (onService ? "online" : "offline"));
    }

    public InetSocketAddress getAddress() {
        return address;
    }
    public long getPollTimeMs() {
        return pollTimeMs;
    }
    public long getGraceTimeMs() {
        return graceTimeMs;
    }

    @Override
    public String toString() {
        return "ServiceStatusCaller{" +
                "address=" + address +
                ", pollTimeMs=" + pollTimeMs +
                ", graceTimeMs=" + graceTimeMs +
                '}';
    }
}
