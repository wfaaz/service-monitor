package wfaaz.monitor;

import org.apache.log4j.Logger;
import wfaaz.Notifier;
import wfaaz.monitor.ping.PingService;
import wfaaz.monitor.ping.PingServiceImpl;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class MonitorJob extends Thread implements Job {
    private static Logger log = Logger.getLogger(MonitorJob.class);
    private SheduleControl sheduleControl;
    private Notifier notifier;
    private PingService pingService;
    private InetSocketAddress serviceAddress;
    private volatile long pollTimeMs;

    public MonitorJob(InetSocketAddress serviceAddress, long pollTimeMs, SheduleControl sheduleControl, Notifier notifier) {
        this.serviceAddress = serviceAddress;
        this.pollTimeMs = pollTimeMs;
        this.sheduleControl = sheduleControl;
        this.notifier = notifier;
        this.pingService = new PingServiceImpl();
    }

    public long getPollTimeMs() {
        return pollTimeMs;
    }

    public void setPollTimeMs(long pollTimeMs) {
        this.pollTimeMs = pollTimeMs;
    }

    @Override
    public void run() {
        log.debug("Monitor job:" + this + "starting...");

        if (!sheduleControl.isOutage(serviceAddress)) {
            notifier.doNotify(serviceAddress, pingService.ping(serviceAddress));
        } else {
            log.trace("Service:" + serviceAddress + "on outage. Monitoring skipped.");
        }

        sheduleControl.shedule(serviceAddress, this);

        log.debug("Monitor job:" + this + "finished.");
    }

    @Override
    public String toString() {
        return "MonitorJob{" +
                "serviceAddress=" + serviceAddress +
                ", pollTimeMs=" + pollTimeMs +
                '}';
    }
}
