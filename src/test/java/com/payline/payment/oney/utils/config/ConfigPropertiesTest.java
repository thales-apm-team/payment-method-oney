package com.payline.payment.oney.utils.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigPropertiesTest {

    @Test
    public void getFromKeyKO() {
        String key = ConfigProperties.get("BadKey");
        Assertions.assertNull(key);

    }

    @Test
    public void getFromKeyOK() {
        String key = ConfigProperties.get("paymentMethod.name");
        Assertions.assertNotNull(key);
    }

}
