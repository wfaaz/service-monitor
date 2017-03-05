package wfaaz.monitor;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface Monitor extends CallerListener {
    void register(InetSocketAddress serviceAddress, long pollTimeSec);
}
