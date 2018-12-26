package com.payline.payment.oney.utils.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigPropertiesTest {

    private ConfigProperties configProperties;
    private String key;



    @Test
    public void getFromKeyKO() {
        key = ConfigProperties.get("BadKey");
        Assertions.assertNull(key);

    }

    @Test
    public void getFromKeyOK() {
        key = ConfigProperties.get("paymentMethod.name");
        Assertions.assertNotNull(key);
    }

}
