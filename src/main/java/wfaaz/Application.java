package wfaaz;

import org.apache.log4j.Logger;
import wfaaz.call.Caller;
import wfaaz.call.CallerListener;
import wfaaz.call.CallerScanner;
import wfaaz.monitor.Monitor;
import wfaaz.monitor.ServiceMonitor;
import wfaaz.outage.ServiceOutageRegister;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class Application {
    private static Logger log = Logger.getLogger(Application.class);

    private final static String WELCOME_MESSAGE ="Welcome to Service-Monitor application.\n";
    private final static String GOODBYE_MESSAGE ="Program is shutting down. Goodbye.\n";

    private static CallerScanner callerScanner;
    private static Monitor monitor;
    private static Notifier notifier;
    private static ServiceOutageRegister outageRegister;

    public static void main(String[] args) {
        log.info(WELCOME_MESSAGE);
        try {
            addShutdownHook();
            init();
            callerScanner.startPoll();
        } catch (Exception ex) {
            log.error("Main app processing failed.", ex);
        }
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info(GOODBYE_MESSAGE);
            }
        });
    }

    private static void init() throws Exception {
        notifier = new NotifierImpl();
        outageRegister = new ServiceOutageRegister();
        monitor = new ServiceMonitor(notifier, outageRegister);

        callerScanner = new CallerScanner(new CallerListener() {
            public void onNewCaller(Caller caller) {
                monitor.registerCaller(caller.getAddress(), caller.getPollTimeMs());
                notifier.registerCaller(caller);
            }
        }, outageRegister);
    }
}
