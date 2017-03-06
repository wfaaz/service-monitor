package wfaaz;

import wfaaz.call.Caller;
import wfaaz.monitor.ping.PingResult;

import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class NotifierImpl implements Notifier {
    public void registerCaller(Caller serviceToNotify) {

    }

    public void doNotify(InetSocketAddress serviceAddress, PingResult pingResult) {
        System.out.print("NOTIFY:" + serviceAddress + pingResult);
    }
}
