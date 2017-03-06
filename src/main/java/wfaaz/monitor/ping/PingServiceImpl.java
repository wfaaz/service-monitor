package wfaaz.monitor.ping;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class PingServiceImpl implements PingService {
    private Logger log = Logger.getLogger(PingServiceImpl.class);

    public PingResult ping(InetSocketAddress serviceAddress) {
        Socket socket = null;
        PingResult pingResult = null;
        long currentTimeMs = System.currentTimeMillis();
        try {
            socket = new Socket(serviceAddress.getAddress(), serviceAddress.getPort());
            pingResult = new PingResultImpl(socket.isConnected(), currentTimeMs);
        } catch (ConnectException ce) {
            if (!ce.getMessage().contains("Connection refused")) {
                log.error(getErrMessage(serviceAddress), ce);
            }
        } catch (Exception ex) {
            log.error(getErrMessage(serviceAddress), ex);
        } finally {
            if (pingResult == null) {
                pingResult = new PingResultImpl(false, currentTimeMs);
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException iox) {
                    log.error("On close socket to " + serviceAddress, iox);
                }
            }
        }
        return pingResult;
    }

    private String getErrMessage(InetSocketAddress serviceAddress) {
        return "On create And connect socket to " + serviceAddress;
    }
}
