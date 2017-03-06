package wfaaz.monitor;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface Monitor {
    void registerCaller(InetSocketAddress serviceAddress, long pollTimeMs);
}
