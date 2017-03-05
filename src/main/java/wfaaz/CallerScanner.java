package wfaaz;

import org.apache.log4j.Logger;
import wfaaz.call.Caller;
import wfaaz.call.ServiceStatusCaller;
import wfaaz.monitor.CallerListener;

import java.net.InetSocketAddress;
import java.util.Scanner;

import static wfaaz.call.validate.ServiceDataValidator.*;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class CallerScanner {
    private static Logger log = Logger.getLogger(Application.class);

    private final static String INPUT_ARGS_FORMAT_MESSAGE = "Input args please. Valid format:\n<ipV4Address>:<port>,<pollTimeOnSeconds>,<graceTimeOnSeconds>";
    private final static String INVALID_ARGS_FORMAT = "The format is invalid.\n";
    private final static String COMMA = ",";
    private final static String COLON = ":";
    private final static long SLEEP_TIMEOUT = 500;

    private Scanner scanner;
    private CallerListener callerListener;
    private String ip;
    private int port;
    private long pollTime;
    private long graceTime;

    public CallerScanner(CallerListener callerListener) {
        this.callerListener = callerListener;
        this.scanner = new Scanner(System.in);
    }

    public void startPoll() throws InterruptedException {
        log.info(INPUT_ARGS_FORMAT_MESSAGE);

        while (!Thread.currentThread().isInterrupted()) {
            String line = scanner.nextLine().trim();
            validateLineEmptiness(line);
            parceLine(line);
        }
    }

    private void validateLineEmptiness(String line) throws InterruptedException {
        if (line == null) {
            try {
                Thread.sleep(SLEEP_TIMEOUT);
            } catch (InterruptedException ie) {
                log.warn("Interrupted on new Callers poll.", ie);
                throw ie;
            }
        }
    }

    private void parceLine(String line) {
        try {
            throwWrapper(isValidArgSeparators(line));

            String[] addressPollAndGraceParams = parceAddressPollAndGrace(line);
            String[] addressAndPort = parceAddressAndPort(addressPollAndGraceParams);

            parceIpAddress(addressAndPort);
            parcePort(addressAndPort);
            parcePollTime(addressPollAndGraceParams);
            parceGraceTime(addressPollAndGraceParams);

            Caller caller = new ServiceStatusCaller(new InetSocketAddress(ip, port), pollTime, graceTime);
            callerListener.onNewCaller(caller);
            log.info("New Caller:" + caller + "added.");
        } catch (ServiceDataValidatorException sdve) {
            log.info(INVALID_ARGS_FORMAT + INPUT_ARGS_FORMAT_MESSAGE);
        }
    }

    private String[] parceAddressPollAndGrace(String line) throws ServiceDataValidatorException {
        String[] addressPollAndGrace = line.split(COMMA);
        throwWrapper(isValidArgNumAddrPollAndGraceTime(addressPollAndGrace));
        return addressPollAndGrace;
    }

    private String[] parceAddressAndPort(String[] addressPollAndGraceParams) throws ServiceDataValidatorException {
        String[] addressAndPort = addressPollAndGraceParams[0].split(COLON);
        throwWrapper(isValidArgNumInetAddr(addressAndPort));
        return addressAndPort;
    }

    private void parceGraceTime(String[] addressPollAndGraceParams) throws ServiceDataValidatorException {
        graceTime = Long.parseLong(addressPollAndGraceParams[2]);
        throwWrapper(isValidTime(graceTime));
    }

    private void parcePollTime(String[] addressPollAndGraceParams) throws ServiceDataValidatorException {
        pollTime = Long.parseLong(addressPollAndGraceParams[1]);
        throwWrapper(isValidTime(pollTime));
    }

    private void parcePort(String[] addressAndPort) throws ServiceDataValidatorException {
        port = Integer.parseInt(addressAndPort[1]);
        throwWrapper(isValidPort(port));
    }

    private void parceIpAddress(String[] addressAndPort) throws ServiceDataValidatorException {
        ip = addressAndPort[0];
        throwWrapper(isValidIpAddress(ip));
    }
}
