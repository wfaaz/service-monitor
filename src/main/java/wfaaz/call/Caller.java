package wfaaz.call;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface Caller {
    void notify(boolean onService);

    InetSocketAddress getAddress();
    long getPollTimeSec();
    long getGraceTimeSec();
}
