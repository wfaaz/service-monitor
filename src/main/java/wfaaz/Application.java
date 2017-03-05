package wfaaz;

import org.apache.log4j.Logger;
import wfaaz.monitor.Monitor;

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
        monitor = null; //fixme
        callerScanner = new CallerScanner(monitor);
        notifier = null; //fixme
        //throw new Exception("Not implemented yet.");
    }
}
