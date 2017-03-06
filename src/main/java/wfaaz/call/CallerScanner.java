package wfaaz.call;

import org.apache.log4j.Logger;
import wfaaz.outage.OutageListener;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static wfaaz.call.validate.ServiceDataValidator.*;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class CallerScanner {
    private static Logger log = Logger.getLogger(CallerScanner.class);

    private final static String INPUT_ARGS_FORMAT_MESSAGE = "Input args please. Valid format:\n" +
            "1) <ipV4Address>:<port>,<pollTimeOnSeconds>,<graceTimeOnSeconds>\n" +
            "where pollTime and pollTime > 1 sec;\n" +
            "2) out <ipV4Address>:<port>,<outageStartTime>,<outageEndTime>";

    private final static String INVALID_ARGS_FORMAT = "The format is invalid.\n";
    private final static String COMMA = ",";
    private final static String COLON = ":";
    private final static String OUT_COMMAND = "out";
    private final static long SLEEP_TIMEOUT = 500;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private Scanner scanner;
    private CallerListener callerListener;
    private OutageListener outageListener;
    private String ip;
    private int port;
    private long pollTimeMs;
    private long graceTimeMs;
    private Date startOutageTimeMs;
    private Date endOutageTimeMs;

    public CallerScanner(CallerListener callerListener, OutageListener outageListener) {
        this.callerListener = callerListener;
        this.outageListener = outageListener;
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

            boolean isOutageParams = line.startsWith(OUT_COMMAND);
            if (isOutageParams) {
                line = line.replaceFirst(OUT_COMMAND, "").trim();
            }
            parceLineParams(line, isOutageParams);

            InetSocketAddress address = new InetSocketAddress(ip, port);
            if (!isOutageParams) {
                Caller caller = new ServiceStatusCaller(address, pollTimeMs, graceTimeMs);
                callerListener.onNewCaller(caller);
                log.info("New Caller:" + caller + "added.");
            } else {
                log.info("New Outage:" + address + "added, period from " + startOutageTimeMs + " to " + endOutageTimeMs);
                outageListener.onOutage(address, startOutageTimeMs, endOutageTimeMs);
            }

        } catch (ServiceDataValidatorException sdve) {
            log.info(INVALID_ARGS_FORMAT + INPUT_ARGS_FORMAT_MESSAGE);
        }
    }

    private void parceLineParams(String line, boolean isOutageParams) throws ServiceDataValidatorException {
        String[] addressAndTimeParams = parceAddressAndTiming(line);
        String[] addressAndPort = parceAddressAndPort(addressAndTimeParams[0]);

        parceIpAddress(addressAndPort);
        parcePort(addressAndPort);
        if (!isOutageParams) {
            parcePollTimeMs(addressAndTimeParams[1]);
            parceGraceTimeMs(addressAndTimeParams[2]);
        } else {
            startOutageTimeMs = parseOutageTiming(addressAndTimeParams[1]);
            endOutageTimeMs = parseOutageTiming(addressAndTimeParams[2]);
        }
    }

    private String[] parceAddressAndTiming(String line) throws ServiceDataValidatorException {
        String[] addressPollAndGrace = line.split(COMMA);
        throwWrapper(isValidArgNumAddrAndTiming(addressPollAndGrace));
        return addressPollAndGrace;
    }

    private String[] parceAddressAndPort(String address) throws ServiceDataValidatorException {
        String[] addressAndPort = address.split(COLON);
        throwWrapper(isValidArgNumInetAddr(addressAndPort));
        return addressAndPort;
    }

    private void parceGraceTimeMs(String graceTime) throws ServiceDataValidatorException {
        throwWrapper(isDigits(graceTime));
        graceTimeMs = TimeUnit.SECONDS.toMillis(Long.parseLong(graceTime));
        throwWrapper(isValidTimeMs(graceTimeMs));
    }

    private void parcePollTimeMs(String pollTime) throws ServiceDataValidatorException {
        throwWrapper(isDigits(pollTime));
        pollTimeMs = TimeUnit.SECONDS.toMillis(Long.parseLong(pollTime));
        throwWrapper(isValidTimeMs(pollTimeMs));
    }

    private void parcePort(String[] addressAndPort) throws ServiceDataValidatorException {
        port = Integer.parseInt(addressAndPort[1]);
        throwWrapper(isValidPort(port));
    }

    private void parceIpAddress(String[] addressAndPort) throws ServiceDataValidatorException {
        ip = addressAndPort[0];
        throwWrapper(isValidIpAddress(ip));
    }

    private Date parseOutageTiming(String outageSdfTime) throws ServiceDataValidatorException {
        throwWrapper(isValidSimpleDateFormat(outageSdfTime));
        try {
            Calendar trueCalendar = Calendar.getInstance();
            Calendar croppedCalendar = Calendar.getInstance();

            croppedCalendar.setTime(sdf.parse(outageSdfTime));
            croppedCalendar.add(Calendar.YEAR, trueCalendar.get(Calendar.YEAR) - 1970);
            croppedCalendar.add(Calendar.DAY_OF_YEAR, trueCalendar.get(Calendar.DAY_OF_YEAR) - 1);

            return croppedCalendar.getTime();
        } catch (ParseException pe) {
            log.warn("On parseOutageTiming", pe);
            throw new ServiceDataValidatorException();
        }
    }
}
