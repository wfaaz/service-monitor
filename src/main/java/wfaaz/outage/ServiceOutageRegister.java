package wfaaz.outage;

import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class ServiceOutageRegister implements OutageListener {
    private Logger log = Logger.getLogger(ServiceOutageRegister.class);
    private Map<InetSocketAddress, OutagePeriod> addressToPeriod = new HashMap<InetSocketAddress, OutagePeriod>();

    public void onOutage(InetSocketAddress serviceAddress, Date startTimeMs, Date endTimeMs) {
        addressToPeriod.put(serviceAddress, new OutagePeriod(startTimeMs, endTimeMs));
        filterFromOldData();
    }

    public OutagePeriod get(InetSocketAddress address) {
        return addressToPeriod.get(address);
    }


    private void filterFromOldData() {
        final long currentTimeMs = System.currentTimeMillis();
        Iterator<Map.Entry<InetSocketAddress, OutagePeriod>> iter = addressToPeriod.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<InetSocketAddress, OutagePeriod> addrToPeriod = iter.next();
            if (currentTimeMs > addrToPeriod.getValue().getEndTimeMs().getTime()) {
                log.debug("Removing old serviceOutage map entry=" + addrToPeriod.getKey() + ", " + addrToPeriod.getValue());
                iter.remove();
            }
        }
    }

    public static class OutagePeriod {
        private Date startTimeMs;
        private Date endTimeMs;

        public OutagePeriod(Date startTimeMs, Date endTimeMs) {
            this.startTimeMs = startTimeMs;
            this.endTimeMs = endTimeMs;
        }

        public Date getStartTimeMs() {
            return startTimeMs;
        }

        public Date getEndTimeMs() {
            return endTimeMs;
        }

        @Override
        public String toString() {
            return "OutagePeriod{" +
                    "startTimeMs=" + startTimeMs +
                    ", endTimeMs=" + endTimeMs +
                    '}';
        }
    }
}
