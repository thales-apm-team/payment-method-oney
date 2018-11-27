package com.payline.payment.oney.service;

import com.payline.payment.oney.service.impl.ConfigurationServiceImpl;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static com.payline.payment.oney.utils.OneyConstants.*;

public class ConfigurationServiceImplTest {

    private ConfigurationServiceImpl service = new ConfigurationServiceImpl();

    @Test
    public void testGetParameters() {

        List<AbstractParameter> parameters = service.getParameters(Locale.FRANCE);
        //Assert we have 3 parameters
        Assert.assertNotNull(parameters);
        Assert.assertEquals(3, parameters.size());
        Assert.assertEquals(X_ONEY_AUTHORIZATION_KEY, parameters.get(0).getKey());
        Assert.assertEquals(PSP_ID_KEY, parameters.get(1).getKey());
        Assert.assertEquals(MERCHANT_GUID_KEY, parameters.get(2).getKey());

    }


}
