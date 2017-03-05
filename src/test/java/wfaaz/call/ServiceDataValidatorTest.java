package wfaaz.call;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wfaaz.call.validate.ServiceDataValidator;

/**
 * Created by wfAaz on 05.03.2017.
 */
public class ServiceDataValidatorTest {
    final String VALID_IP = "167.23.12.10";
    final String INVALID_IP = "SOMEtext123";

    final int VALID_PORT = 8080;
    final int LESS_THEN_MAX_VALID_PORT = 70535;
    final int MINUS_INVALID_PORT = -6;

    final long VALID_TIMEOUT = 5;
    final long INVALID_TIMEOUT = -453;

    @Test
    public void validIpShouldReturnTrue() {
        Assert.assertTrue(ServiceDataValidator.validateIpAddress(VALID_IP));
    }

    @Test
    public void invalidIpShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.validateIpAddress(INVALID_IP));
    }

    @Test
    public void nullIpShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.validateIpAddress(null));
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void validPortShouldReturnTrue() {
        Assert.assertTrue(ServiceDataValidator.validatePort(VALID_PORT));
    }

    @Test
    public void lessThenMaxPortShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.validatePort(LESS_THEN_MAX_VALID_PORT));
    }

    @Test
    public void minusPortShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.validatePort(MINUS_INVALID_PORT));
    }


    //// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void validTimeShouldReturnTrue() {
        Assert.assertTrue(ServiceDataValidator.validateTime(VALID_TIMEOUT));
    }

    @Test
    public void invalidTimeShouldReturnFalse() {
        Assert.assertFalse(ServiceDataValidator.validateTime(INVALID_TIMEOUT));
    }
}