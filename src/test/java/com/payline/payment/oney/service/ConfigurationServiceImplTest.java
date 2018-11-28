package com.payline.payment.oney.service;

import com.payline.payment.oney.service.impl.ConfigurationServiceImpl;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.TestUtils.createContractConfiguration;
import static com.payline.payment.oney.utils.TestUtils.createDefaultEnvironment;

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
    @Test
    public void checkOK(){

        Map<String,String> accountInfo = new HashMap<>();
        accountInfo.put(X_ONEY_AUTHORIZATION_KEY,"mykey");
        accountInfo.put(PSP_ID_KEY,"psp_id_test");
        accountInfo.put(MERCHANT_GUID_KEY,"merchant_guid_test");



        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
        .aCheckRequest()
        .withAccountInfo(accountInfo)
        .withLocale(Locale.FRANCE)
        .withContractConfiguration(createContractConfiguration())
        .withEnvironment(createDefaultEnvironment())
        .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assert.assertEquals(0, errors.size());

    }

    @Test
    public void checkKO(){

        Map<String,String> accountInfo = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(accountInfo)
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(createContractConfiguration())
                .withEnvironment(createDefaultEnvironment())
                .build();
        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assert.assertEquals(3, errors.size());
        Assert.assertNotNull(errors.get(X_ONEY_AUTHORIZATION_KEY));
        Assert.assertNotNull(errors.get(PSP_ID_KEY));
        Assert.assertNotNull(errors.get(MERCHANT_GUID_KEY));



    }
}
