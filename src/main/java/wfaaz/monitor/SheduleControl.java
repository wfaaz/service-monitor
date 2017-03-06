package wfaaz.monitor;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 06.03.2017.
 */
public interface SheduleControl {
    void shedule(InetSocketAddress serviceAddress, Job job);
    boolean isOutage(InetSocketAddress serviceAddress);
}
