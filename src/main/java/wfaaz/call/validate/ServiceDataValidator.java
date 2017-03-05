package wfaaz.call.validate;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceDataValidator {
    private static Logger log = Logger.getLogger(ServiceDataValidator.class);
    private static Pattern VALID_IPV4_PATTERN = null;
    private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static int MAX_VALID_PORT = 65535;
    private static String COLON = ":";
    private static String COMMA = ",";

    static {
        try {
            VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
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
            return VALID_IPV4_PATTERN.matcher(ipAddress).matches();
        }
        return false;
    }

    public static boolean isValidPort(int port) {
        return port >= 0 && port <= MAX_VALID_PORT;
    }

    public static boolean isValidTime(long time) {
        return time >= 0;
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static boolean isValidArgSeparators(String line) {
        return !line.isEmpty() && line.contains(COMMA) && line.contains(COLON);
    }

    public static boolean isValidArgNumInetAddr(String [] array) {
        return isValidNumOfArgs(array, 2);
    }

    public static boolean isValidArgNumAddrPollAndGraceTime(String [] array) {
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
