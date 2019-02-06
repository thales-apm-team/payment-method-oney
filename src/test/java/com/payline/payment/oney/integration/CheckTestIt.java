package com.payline.payment.oney.integration;

import com.payline.payment.oney.service.impl.ConfigurationServiceImpl;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.payline.payment.oney.utils.OneyConstants.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckTestIt {

    public static final String IDENTIFIER = "Oney";
    public static final String MERCHANT_KEY = "9813e3ff-c365-43f2-8dca-94b850befbf9";
    public static final String AUTHOR_KEY = "7fd3f1c53b9a47f7b85c801a32971895";
    public static final String PSP_KEY = "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a";
    public static final String ONEY_API_URL = "https://oney-staging.azure-api.net";
    public static final ContractProperty CHIFFREMENT_KEY = new ContractProperty("66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");
    public static final String METHOD_BODY = "Method-body";
    public static final String COUNTRY_CODE = "BE";
    public static final String LANG_CODE = "fr";

    public static final String TEST_PARTNER_AUTHORIZATION_KEY = PARTNER_AUTHORIZATION_KEY + ".be";
    public static final String TEST_PSP_GUID_KEY = PSP_GUID_KEY + ".be";

    private ConfigurationServiceImpl service = new ConfigurationServiceImpl();

    private Environment environment;

    @BeforeAll
    public void setUp() {
        environment = new Environment("https://succesurl.com/", "http://redirectionURL.com", "http://redirectionCancelURL.com", true);
    }

    @Test
    public void checkOK() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty(COUNTRY_CODE)); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(0, errors.size());

    }

    @Test
    public void checkKo_BadAuthorization() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty(COUNTRY_CODE)); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, "badPartnerAuthorization");
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_AUTHORIZATION_KEY));

    }

    @Test
    public void checkKo_BadPartnerCountryCode() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("badPartnerCountryCode")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertTrue(errors.size() >= 1);
        Assertions.assertTrue(errors.keySet().contains(COUNTRY_CODE_KEY));

    }

    @Test
    public void checkKo_BadPartnerCountryCode2() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        // code country correct, mais qui retourne une erreur bad request ...
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("FR")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertTrue(errors.size() >= 1);
        Assertions.assertTrue(errors.keySet().contains(PARTNER_AUTHORIZATION_KEY));
        Assertions.assertTrue(errors.keySet().contains(PSP_GUID_KEY));

    }

    @Test
    public void checkKo_BadPartnerCountryCode3() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        // code country correct, mais qui retourne une erreur bad request ...
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("ZW")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertTrue(errors.size() >= 1);
        Assertions.assertTrue(errors.keySet().contains(PARTNER_AUTHORIZATION_KEY));
        Assertions.assertTrue(errors.keySet().contains(PSP_GUID_KEY));

    }

    @Test
    public void checkKo_BadUrl() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty(COUNTRY_CODE)); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("zu"));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, "https://oney3x-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertTrue(errors.size() >= 1);
        Assertions.assertTrue(errors.keySet().contains(PARTNER_API_URL));

    }

    @Test
    public void checkKo_BadUrl2() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("2"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty(COUNTRY_CODE)); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("zu"));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, "https://google.com");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_API_URL));

    }

    @Test
    public void checkKo_badChiffrementKey() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty(COUNTRY_CODE)); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY,
                new ContractProperty("66s581CG5W+RLEqZHAGQx+vskjy660Kx8rhtRpXtY="));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(PARTNER_CHIFFREMENT_KEY));

    }

    @Test
    public void checkKo_badOpcKey() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration(IDENTIFIER, new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty(MERCHANT_KEY));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3z002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty(COUNTRY_CODE)); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty(LANG_CODE));
        contractConfiguration.getContractProperties().put(PARTNER_CHIFFREMENT_KEY, CHIFFREMENT_KEY);

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(TEST_PSP_GUID_KEY, PSP_KEY);
        partnerConfiguration.put(SECRET_KEY, METHOD_BODY);
        partnerConfiguration.put(TEST_PARTNER_AUTHORIZATION_KEY, AUTHOR_KEY);
        partnerConfiguration.put(PARTNER_API_URL, ONEY_API_URL);

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(toAccountInfo(contractConfiguration))
                .withLocale(Locale.FRANCE)
                .withContractConfiguration(contractConfiguration)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withEnvironment(environment)
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(OPC_KEY));

    }


    private Map<String, String> toAccountInfo(ContractConfiguration contractConfiguration) {
        Map<String, String> accountInfo = new HashMap<>();
        for (String key : contractConfiguration.getContractProperties().keySet()) {
            accountInfo.put(key, contractConfiguration.getContractProperties().get(key).getValue());
        }
        return accountInfo;
    }
}
