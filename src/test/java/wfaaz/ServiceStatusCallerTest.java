package wfaaz;

import org.junit.Assert;
import org.junit.Test;
import java.net.InetSocketAddress;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceStatusCallerTest {
    final static InetSocketAddress ADDRESS = new InetSocketAddress("127.0.0.1", 8888);
    final static long POLL_TIME_SEC = 5;
    final static long GRACE_TIME_SEC = 20;

    @Test
    public void pojoTest() {
        ServiceStatusCaller caller = new ServiceStatusCaller(ADDRESS, POLL_TIME_SEC, GRACE_TIME_SEC);
        caller.notify(true);
        Assert.assertEquals(caller.getAddress(), ADDRESS);
        Assert.assertEquals(caller.getGraceTimeSec(), GRACE_TIME_SEC);
        Assert.assertEquals(caller.getPollTimeSec(), POLL_TIME_SEC);
    }
}
