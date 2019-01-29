package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.*;
import java.util.regex.Pattern;

import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.TestUtils.createDefaultEnvironment;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigurationServiceImplTest {

    @InjectMocks
    private ConfigurationServiceImpl service;

    @Spy
    OneyHttpClient httpClient;

    private Map<String, String> accountInfo = new HashMap<>();

    @BeforeAll
    public void setUp() throws Exception {
        service = new ConfigurationServiceImpl();
        MockitoAnnotations.initMocks(this);

        StringResponse responseMockedPending = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ul3aakmok0anPtpBvW1vZ3e83c7evaIMgKsuqlJpPjg407AoMkFm94736cZcnpC81qiX4V8n9IxMD1E50QBAOkMZ1S8Pf90kxhXSDe3wt4J13\"}");
        // La partie HTTP call estr traitée dans les tests d'intégration.
        Mockito.doReturn(responseMockedPending).when(httpClient).initiateCheckPayment(Mockito.anyString(), Mockito.anyMap());
    }

    @Test
    public void testGetParametersKeys() {
        List<AbstractParameter> parameters = service.getParameters(Locale.FRANCE);
        Pattern p = Pattern.compile("[a-zA-Z]*");
        for (AbstractParameter param : parameters) {
            Assertions.assertTrue(p.matcher(param.getKey()).matches(), param.getKey() + " comporte des caracteres interdits");
        }
    }

    @Test
    public void testGetParameters() {

        List<AbstractParameter> parameters = service.getParameters(Locale.FRANCE);
        //Assert we have 3 parameters
        Assertions.assertNotNull(parameters);
        Assertions.assertEquals(9, parameters.size());

        List<String> result = new ArrayList<>();
        for (AbstractParameter paramter : parameters) {
            result.add(paramter.getKey());
        }
        Assertions.assertTrue(result.contains(X_ONEY_AUTHORIZATION_KEY));
        Assertions.assertTrue(result.contains(PSP_GUID_KEY));
        Assertions.assertTrue(result.contains(MERCHANT_GUID_KEY));
        Assertions.assertTrue(result.contains(API_MARKETING_KEY));
        Assertions.assertTrue(result.contains(OPC_KEY));
        Assertions.assertTrue(result.contains(NB_ECHEANCES_KEY));
        Assertions.assertTrue(result.contains(COUNTRY_CODE_KEY));
        Assertions.assertTrue(result.contains(LANGUAGE_CODE_KEY));
        Assertions.assertTrue(result.contains(ID_INTERNATIONAL_KEY));

    }

    @Test
    public void checkOK() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("merchant_guid_test"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3z002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("FR")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "psp_id_test");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHORIZATION_KEY, "mykey");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(accountInfo)
                .withLocale(Locale.FRANCE)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(createDefaultEnvironment())
                .build();

        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(0, errors.size());

    }

    @Test
    public void checkCountryKO() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("merchant_guid_test"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3z002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "psp_id_test");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHORIZATION_KEY, "mykey");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");


        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(accountInfo)
                .withLocale(Locale.FRANCE)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(createDefaultEnvironment())
                .build();
        contractParametersCheckRequest.getContractConfiguration().getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("BEL"));
        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(COUNTRY_CODE_DESCRIPTION));
    }

    @Test
    public void checkLanguageKO() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(MERCHANT_GUID_KEY, new ContractProperty("merchant_guid_test"));
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3z002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("FR"));
        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(PSP_GUID_KEY, "psp_id_test");
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_AUTHORIZATION_KEY, "mykey");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");


        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(accountInfo)
                .withLocale(Locale.FRANCE)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(createDefaultEnvironment())
                .build();
        contractParametersCheckRequest.getContractConfiguration().getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("FR"));
        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.keySet().contains(LANGUAGE_CODE_KEY));
    }

    @Test
    public void checkKO() {

        final ContractConfiguration contractConfiguration = new ContractConfiguration("Oney", new HashMap<>());
        contractConfiguration.getContractProperties().put(OPC_KEY, new ContractProperty("3z002"));
        contractConfiguration.getContractProperties().put(NB_ECHEANCES_KEY, new ContractProperty("x"));
        contractConfiguration.getContractProperties().put(COUNTRY_CODE_KEY, new ContractProperty("FR")); // ouy 3 caractères
        contractConfiguration.getContractProperties().put(LANGUAGE_CODE_KEY, new ContractProperty("fr"));

        Map<String, String> partnerConfiguration = new HashMap<>();
        partnerConfiguration.put(SECRET_KEY, "Method-body");
        partnerConfiguration.put(PARTNER_API_URL, "https://oney-staging.azure-api.net");

        Map<String, String> sensitivePartnerConfiguration = new HashMap<>();
        sensitivePartnerConfiguration.put(PARTNER_CHIFFREMENT_KEY, "66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        ContractParametersCheckRequest contractParametersCheckRequest = ContractParametersCheckRequest.CheckRequestBuilder
                .aCheckRequest()
                .withAccountInfo(accountInfo)
                .withLocale(Locale.FRANCE)
                .withPartnerConfiguration(new PartnerConfiguration(partnerConfiguration, sensitivePartnerConfiguration))
                .withContractConfiguration(contractConfiguration)
                .withEnvironment(createDefaultEnvironment())
                .build();
        Map<String, String> errors = service.check(contractParametersCheckRequest);
        Assertions.assertEquals(3, errors.size());
        Assertions.assertNotNull(errors.get(PARTNER_AUTHORIZATION_KEY));
        Assertions.assertNotNull(errors.get(PSP_GUID_KEY));
        Assertions.assertNotNull(errors.get(MERCHANT_GUID_KEY));
    }

    @Test
    public void testGetReleaseInformation_ok() {
        // when: getReleaseInformation method is called
        ReleaseInformation releaseInformation = service.getReleaseInformation();

        // then: result is not null
        Assertions.assertNotNull(releaseInformation);
        // then: assert release version and release date are not null
        Assertions.assertNotNull(releaseInformation.getVersion());
        Assertions.assertFalse(releaseInformation.getVersion().isEmpty());
        Assertions.assertNotNull(releaseInformation.getDate());
    }

    //
    @Test
    public void testGetReleaseInformation_versionFormat() {
        // when: getReleaseInformation method is called
        ReleaseInformation releaseInformation = service.getReleaseInformation();

        // then: the version has a valid format
        Assertions.assertNotNull(releaseInformation);
        Assertions.assertTrue(releaseInformation.getVersion().matches("^\\d\\.\\d(\\.\\d)?$"));
    }

}
