package wfaaz.monitor;

import org.apache.log4j.Logger;
import wfaaz.Notifier;
import wfaaz.outage.ServiceOutageRegister;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class ServiceMonitor implements Monitor, SheduleControl {
    private static Logger log = Logger.getLogger(ServiceMonitor.class);
    private final int THREAD_POOL_SIZE = 5;

    private Map<InetSocketAddress, Job> addressToMonitoringJobMap;
    private ScheduledExecutorService executorService;
    private ServiceOutageRegister outageRegister;
    private Notifier notifier;

    public ServiceMonitor(Notifier notifier, ServiceOutageRegister outageRegister) {
        this.notifier = notifier;
        this.outageRegister = outageRegister;
        executorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
        addressToMonitoringJobMap = new HashMap<InetSocketAddress, Job>();
    }

    public final void registerCaller(InetSocketAddress serviceAddress, long pollTimeMs) {
        Job job = addressToMonitoringJobMap.get(serviceAddress);
        if (job != null) {
            long previousPollTimeMs = job.getPollTimeMs();
            if (pollTimeMs < previousPollTimeMs) {
                job.setPollTimeMs(pollTimeMs);
            }
        } else {
            Job newJob = new MonitorJob(serviceAddress, pollTimeMs, this, notifier);
            addressToMonitoringJobMap.put(serviceAddress, newJob);
            log.debug("New Job: " + newJob + "created.");

            shedule(serviceAddress, newJob);
        }
    }

    public final void shedule(InetSocketAddress serviceAddress, Job job) {
        final long timeToSheduleJob = getTimeToSheduleJob(serviceAddress, job.getPollTimeMs());
        executorService.schedule(job, timeToSheduleJob, TimeUnit.MILLISECONDS);
        log.debug("Sheduled monitoringJob in" + timeToSheduleJob + "ms");
    }

    public boolean isOutage(InetSocketAddress serviceAddress) {
        ServiceOutageRegister.OutagePeriod outagePeriod = outageRegister.get(serviceAddress);
        if (outagePeriod != null) {
            long currentTimeMs = System.currentTimeMillis();
            return (outagePeriod.getStartTimeMs().getTime() < currentTimeMs)
                    && (outagePeriod.getEndTimeMs().getTime() > currentTimeMs);
        }
        return false;
    }

    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private long getTimeToSheduleJob(InetSocketAddress serviceAddress, long pollTimeMs) {
        final long timeToSheduleJob;
        if (isOutage(serviceAddress)) {
            timeToSheduleJob = outageRegister.get(serviceAddress).getEndTimeMs().getTime() - System.currentTimeMillis();
        } else {
            timeToSheduleJob = pollTimeMs;
        }
        return timeToSheduleJob;
    }
}
