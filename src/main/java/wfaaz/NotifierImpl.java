package wfaaz;

import org.apache.log4j.Logger;
import wfaaz.call.Caller;
import wfaaz.call.TimedStatefulWrappedCaller;
import wfaaz.call.TimedStatefulWrappedCaller.State;
import wfaaz.monitor.ping.PingResult;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class NotifierImpl implements Notifier {
    private Logger log = Logger.getLogger(NotifierImpl.class);
    private Map<InetSocketAddress, TimedStatefulWrappedCaller> addressToCallerMap;

    public NotifierImpl() {
        addressToCallerMap = new HashMap<>();
    }

    public void registerCaller(Caller serviceToNotify) {
        addressToCallerMap.put(serviceToNotify.getAddress(), new TimedStatefulWrappedCaller(serviceToNotify));
    }

    public void doNotify(InetSocketAddress serviceAddress, PingResult pingResult) {
        TimedStatefulWrappedCaller timedCaller = addressToCallerMap.get(serviceAddress);
        if (timedCaller == null) {
            String errMsg = "Service:" + serviceAddress + "is not present on the Notifier Callers Map.";
            log.error(errMsg);
        } else {
            boolean onService = pingResult.isOnService();
            long currenTime = System.currentTimeMillis();
            long graceTime = timedCaller.getCaller().getGraceTimeMs();
            State previousState = timedCaller.getState();

            switch (previousState) {
                case OFFLINE:
                    if (onService) {
                        if (currenTime > (timedCaller.getOfflineTime() - 1 + graceTime)) {
                            timedCaller.getCaller().doNotify(onService);
                            timedCaller.setStateOnlineAndFlush();
                        } else {
                            timedCaller.setOfflineIfEmpty(pingResult.getProcessingTimeMs());
                        }
                    }
                    break;
                case ONLINE:
                    if (!onService) {
                        timedCaller.setOfflineIfEmpty(pingResult.getProcessingTimeMs());
                        if (currenTime > (timedCaller.getOfflineTime() - 1 + graceTime)) {
                            timedCaller.getCaller().doNotify(onService);
                            timedCaller.setStateOfflineAndUpdateOfflineTImeIfEmpty(pingResult.getProcessingTimeMs());
                        }
                    }
                    break;
                default: // UNDEFINED
                    timedCaller.getCaller().doNotify(onService);
                    if (onService) {
                        timedCaller.setStateOnlineAndFlush();
                    } else {
                        timedCaller.setStateOfflineAndUpdateOfflineTImeIfEmpty(pingResult.getProcessingTimeMs());
                    }
            }
        }
    }
}
