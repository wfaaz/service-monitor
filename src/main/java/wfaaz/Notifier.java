package wfaaz;

import wfaaz.call.Caller;
import wfaaz.monitor.ping.PingResult;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public interface Notifier {
    void registerCaller(Caller serviceToNotify);
    void doNotify(InetSocketAddress serviceAddress, PingResult pingResult);

}
