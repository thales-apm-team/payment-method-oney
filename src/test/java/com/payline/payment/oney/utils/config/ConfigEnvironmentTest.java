package com.payline.payment.oney.utils.config;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ConfigEnvironmentTest {


    private ConfigEnvironment environment;

    @Test
    public void testEnvDev(){
        environment = ConfigEnvironment.DEV;
        String prefix = environment.getPrefix();

        Assert.assertNotNull(environment);
        Assert.assertEquals("dev",prefix);
    }

    @Test
    public void testEnvProd(){
        environment = ConfigEnvironment.PROD;
        String prefix = environment.getPrefix();

        Assert.assertNotNull(environment);
        Assert.assertEquals("prod",prefix);
    }


}
