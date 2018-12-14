package com.payline.payment.oney.utils.config;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigPropertiesTest {

    private ConfigProperties configProperties;
    private String key;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getFromKeyKO() {
        key = ConfigProperties.get("BadKey");
        Assert.assertNull(key);

    }

    @Test
    public void getFromKeyOK() {
        key = ConfigProperties.get("paymentMethod.name");
        Assert.assertNotNull(key);
    }

}
