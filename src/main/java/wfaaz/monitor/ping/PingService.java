package wfaaz.monitor.ping;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface PingService {
    PingResult ping(InetSocketAddress serviceAddress);
}
