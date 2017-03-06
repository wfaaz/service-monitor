package wfaaz.outage;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Created by wfAaz on 06.03.2017.
 */
public interface OutageListener {
    void onOutage(InetSocketAddress serviceAddress, Date start, Date end);
}
