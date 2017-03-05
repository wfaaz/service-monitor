package wfaaz;

import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceStatusCaller implements Caller {
    private Logger log = Logger.getLogger(ServiceStatusCaller.class);
    private InetSocketAddress address;
    private long pollTimeSec;
    private long graceTimeSec;

    public ServiceStatusCaller(InetSocketAddress address, long pollTimeSec, long graceTimeSec) {
        this.address = address;
        this.pollTimeSec = pollTimeSec;
        this.graceTimeSec = graceTimeSec;
    }

    public void notify(String message) {
        log.info("Service:" + address + " status=" + message);
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public long getPollTimeSec() {
        return pollTimeSec;
    }

    public long getGraceTimeSec() {
        return graceTimeSec;
    }
}
