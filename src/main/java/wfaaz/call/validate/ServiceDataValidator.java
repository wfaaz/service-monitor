package wfaaz.call.validate;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceDataValidator {
    private static Logger log = Logger.getLogger(ServiceDataValidator.class);

    private static final String IPV4_REG_EXP = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final String SDF_REG_EXP = "(^([0-1]?\\d|2[0-3]):([0-5]?\\d):([0-5]?\\d)$)|(^([0-5]?\\d):([0-5]?\\d)$)|(^[0-5]?\\d$)";

    private static final String DIGIT_REG_EXP = "[0-9]+";
    private final static long MIN_TIME_MS = 1000;

    private static Pattern ipV4Pattern;
    private static Pattern sdfPattern;
    private static Pattern digitPattern;
    private static int MAX_VALID_PORT = 65535;
    private static String COLON = ":";
    private static String COMMA = ",";

    static {
        try {
            ipV4Pattern = Pattern.compile(IPV4_REG_EXP, Pattern.CASE_INSENSITIVE);
            sdfPattern = Pattern.compile(SDF_REG_EXP, Pattern.CASE_INSENSITIVE);
            digitPattern = Pattern.compile(DIGIT_REG_EXP, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            log.error("Unable to compile pattern", e);
        }
    }

    /**
     * Determine if the given string is a valid IPv4 address.  This method
     * uses pattern matching to see if the given string could be a valid IP address.
     *
     * @param ipAddress A string that is to be examined to verify whether or not
     *  it could be a valid IP address.
     * @return <code>true</code> if the string is a value that is a valid IP address,
     *  <code>false</code> otherwise.
     */
    public static boolean isValidIpAddress(String ipAddress) {
        if (ipAddress != null && !ipAddress.trim().isEmpty()) {
            return ipV4Pattern.matcher(ipAddress).matches();
        }
        return false;
    }

    /**
     * Determine if the given string is a valid SimpleDateFormat time.  This method
     * uses pattern matching to see if the given string could be a valid time.
     *
     * @param sdfTime A string that is to be examined to verify whether or not
     *  it could be a valid time on format HH:MM:SS.
     * @return <code>true</code> if the string is a value that is a valid sdf time,
     *  <code>false</code> otherwise.
     */
    public static boolean isValidSimpleDateFormat(String sdfTime) {
        if (sdfTime != null && !sdfTime.trim().isEmpty()) {
            return sdfPattern.matcher(sdfTime).matches();
        }
        return false;
    }

    public static boolean isValidPort(int port) {
        return port >= 0 && port <= MAX_VALID_PORT;
    }

    public static boolean isDigits(String line) {
        if (line != null && !line.trim().isEmpty()) {
            return digitPattern.matcher(line).matches();
        }
        return false;
    }

    public static boolean isValidTimeMs(long time) {
        return time >= MIN_TIME_MS;
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static boolean isValidArgSeparators(String line) {
        return !line.isEmpty() && line.contains(COMMA) && line.contains(COLON);
    }

    public static boolean isValidArgNumInetAddr(String [] array) {
        return isValidNumOfArgs(array, 2);
    }

    public static boolean isValidArgNumAddrAndTiming(String [] array) {
        return isValidNumOfArgs(array, 3);
    }

    private static boolean isValidNumOfArgs(String [] array, int numOfArgs) {
        return array.length == numOfArgs;
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void throwWrapper(boolean valid) throws ServiceDataValidatorException {
        if (!valid) throw  new ServiceDataValidatorException();
    }

    public static class ServiceDataValidatorException extends Exception {
        public ServiceDataValidatorException() {
        }
    }
}
