package wfaaz.call;

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
        caller.doNotify(true);
        Assert.assertEquals(ADDRESS, caller.getAddress());
        Assert.assertEquals(GRACE_TIME_SEC, caller.getGraceTimeMs());
        Assert.assertEquals(POLL_TIME_SEC, caller.getPollTimeMs());
    }
}
