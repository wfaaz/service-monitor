package wfaaz.call;

import org.junit.Assert;
import org.junit.Test;
import wfaaz.call.validate.ServiceDataValidator;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceDataValidatorTest {
    final String VALID_IP = "167.23.12.10";
    final String INVALID_IP = "400.23.22.257";
    final String INVALID_IP_NO_DIGITS_PRESENT = "SOMEtext123";

    final int VALID_PORT = 8080;
    final int LESS_THEN_MAX_VALID_PORT = 70535;
    final int MINUS_INVALID_PORT = -6;

    final long VALID_TIMEOUT = 5000;
    final long INVALID_TIMEOUT = -45300;

    @Test
    public void validIpShouldReturnTrue() {
        Assert.assertTrue(ServiceDataValidator.isValidIpAddress(VALID_IP));
    }

    @Test
    public void invalidIpNoDigitsPresentShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.isValidIpAddress(INVALID_IP_NO_DIGITS_PRESENT));
    }

    @Test
    public void invalidIpBadMaskShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.isValidIpAddress(INVALID_IP));
    }

    @Test
    public void nullIpShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.isValidIpAddress(null));
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void validPortShouldReturnTrue() {
        Assert.assertTrue(ServiceDataValidator.isValidPort(VALID_PORT));
    }

    @Test
    public void lessThenMaxPortShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.isValidPort(LESS_THEN_MAX_VALID_PORT));
    }

    @Test
    public void minusPortShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.isValidPort(MINUS_INVALID_PORT));
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void validTimeShouldReturnTrue() {
        Assert.assertTrue(ServiceDataValidator.isValidTimeMs(VALID_TIMEOUT));
    }

    @Test
    public void invalidTimeShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.isValidTimeMs(INVALID_TIMEOUT));
    }
}