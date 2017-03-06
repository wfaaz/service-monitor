package wfaaz;

import org.junit.Before;
import org.junit.Test;
import wfaaz.call.Caller;
import wfaaz.call.ServiceStatusCaller;
import wfaaz.monitor.ping.PingResultImpl;

import java.net.InetSocketAddress;

import static org.mockito.Mockito.*;

/**
 * Created by wfAaz on 06.03.2017.
 */
public class NotifierImplTest {
    final static InetSocketAddress ADDRESS = new InetSocketAddress("127.0.0.1", 8888);
    final static long POLL_TIME_SEC = 5000;
    final static long GRACE_TIME_SEC = 20000;

    Caller spyCaller;
    NotifierImpl notifier;

    @Before
    public void before() {
        Caller caller = new ServiceStatusCaller(ADDRESS, POLL_TIME_SEC, GRACE_TIME_SEC);
        spyCaller = spy(caller);
        notifier = new NotifierImpl();
    }

    @Test
    public void firstOnServiceOnlineSubmitShouldDoNotifyTrue() {
        notifier.registerCaller(spyCaller);
        notifier.doNotify(ADDRESS, new PingResultImpl(true, System.currentTimeMillis()));
        verify(spyCaller, times(1)).doNotify(true);
    }

    @Test
    public void firstOnServiceOfflineSubmitShouldDoNotifyFalse() {
        notifier.registerCaller(spyCaller);
        notifier.doNotify(ADDRESS, new PingResultImpl(false, System.currentTimeMillis()));
        verify(spyCaller, times(1)).doNotify(false);
    }

    @Test
    public void SwicthFromOnToOffShouldDoNotifyFalse() {
        Caller caller = new ServiceStatusCaller(ADDRESS, POLL_TIME_SEC, 0);
        Caller spyCaller = spy(caller);
        NotifierImpl notifier = new NotifierImpl();

        notifier.registerCaller(spyCaller);
        notifier.doNotify(ADDRESS, new PingResultImpl(true, System.currentTimeMillis()));
        notifier.doNotify(ADDRESS, new PingResultImpl(false, System.currentTimeMillis()-10));
        verify(spyCaller, times(1)).doNotify(false);
    }

    @Test
    public void SwicthFromOnToOnShouldDoNothing() {
        notifier.registerCaller(spyCaller);
        notifier.doNotify(ADDRESS, new PingResultImpl(true, System.currentTimeMillis()));
        verify(spyCaller, times(1)).doNotify(true);
        notifier.doNotify(ADDRESS, new PingResultImpl(true, System.currentTimeMillis()));
        verify(spyCaller, never()).doNotify(false);
    }

    @Test
    public void SwicthFromOffToOnShouldDoNotifyTrue() {
        Caller caller = new ServiceStatusCaller(ADDRESS, POLL_TIME_SEC, -1);
        Caller spyCaller = spy(caller);
        NotifierImpl notifier = new NotifierImpl();

        notifier.registerCaller(spyCaller);
        notifier.doNotify(ADDRESS, new PingResultImpl(false, System.currentTimeMillis()));
        verify(spyCaller, times(1)).doNotify(false);
        notifier.doNotify(ADDRESS, new PingResultImpl(true, System.currentTimeMillis()));
        verify(spyCaller, times(1)).doNotify(true);
    }
}