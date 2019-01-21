package com.payline.payment.oney.integration;

import com.payline.payment.oney.service.impl.ConfigurationServiceImpl;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.TestUtils.createDefaultEnvironment;

public class CheckTestIt {

    private ConfigurationServiceImpl service = new ConfigurationServiceImpl();

    @Test
    public void checkOK() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BE")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(0, errors.size());

    }

    @Test
    public void checkKo_BadAuthorization() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(X_ONEY_AUTHORIZATION_KEY, new ContractProperty("xxx"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BE")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "badPartnerAuthorization");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_AUTHRIZATION_KEY));

    }

    @Test
    public void checkKo_BadPartnerCountryCode() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(X_ONEY_AUTHORIZATION_KEY, new ContractProperty("xxx"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("badPartnerCountryCode")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(COUNTRY_CODE_KEY));

    }

    @Test
    public void checkKo_BadPartnerCountryCode2() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(X_ONEY_AUTHORIZATION_KEY, new ContractProperty("xxx"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        // code country correct, mais qui retourne une erreur bad request ...
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("FR")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));
        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(COUNTRY_CODE_KEY));

    }

    @Test
    public void checkKo_BadPartnerCountryCode3() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(X_ONEY_AUTHORIZATION_KEY, new ContractProperty("xxx"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        // code country correct, mais qui retourne une erreur bad request ...
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("ZW")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(COUNTRY_CODE_KEY));

    }

    @Test
    public void checkKo_BadUrl() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(X_ONEY_AUTHORIZATION_KEY, new ContractProperty("xxx"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BE")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("zu"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney3x-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_API_URL));

    }

    @Test
    public void checkKo_BadUrl2() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(X_ONEY_AUTHORIZATION_KEY, new ContractProperty("xxx"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BE")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("zu"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://google.com");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_API_URL));

    }

    @Test
    public void checkKo_badChiffrementKey() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BE")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kx8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_CHIFFREMENT_KEY));

    }

    @Test
    public void checkKo_badOpcKey() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3z002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BE")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHRIZATION_KEY, "7fd3f1c53b9a47f7b85c801a32971895");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(new HashMap<>())
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(OPC_KEY));

    }
}
