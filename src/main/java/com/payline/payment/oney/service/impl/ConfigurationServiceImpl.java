package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.common.OneyError;
import com.payline.payment.oney.bean.common.OneyError40x;
import com.payline.payment.oney.bean.request.OneyEncryptedRequest;
import com.payline.payment.oney.bean.response.PaymentErrorResponse;
import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.payment.oney.utils.i18n.I18nService;
import com.payline.payment.oney.utils.properties.constants.ConfigurationConstants;
import com.payline.payment.oney.utils.properties.service.ReleasePropertiesEnum;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.InputParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.ListBoxParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.PasswordParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.service.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.payline.payment.oney.bean.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.utils.OneyConstants.*;

public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);

    private I18nService i18n = I18nService.getInstance();

    private OneyHttpClient httpClient = OneyHttpClient.getInstance();

    @Override
    public List<AbstractParameter> getParameters(Locale locale) {
        List<AbstractParameter> parameters = new ArrayList<>();

        // merchant GUID
        final InputParameter merchantGuid = new InputParameter();
        merchantGuid.setKey(MERCHANT_GUID_KEY);
        merchantGuid.setLabel(this.i18n.getMessage(MERCHANT_GUID_LABEL, locale));
        merchantGuid.setDescription(this.i18n.getMessage(MERCHANT_GUID_DESCRIPTION, locale));
        merchantGuid.setRequired(true);
        parameters.add(merchantGuid);

        //Api Key
        final PasswordParameter apiKey = new PasswordParameter();
        apiKey.setKey(X_ONEY_AUTHORIZATION_KEY);
        apiKey.setLabel(this.i18n.getMessage(X_ONEY_AUTHORIZATION_LABEL, locale));
        apiKey.setDescription(this.i18n.getMessage(X_ONEY_AUTHORIZATION_DESCRIPTION, locale));
        apiKey.setRequired(true);
        parameters.add(apiKey);

        // psp guid
        final InputParameter pspGuid = new InputParameter();
        pspGuid.setKey(PSP_GUID_KEY);
        pspGuid.setLabel(this.i18n.getMessage(PSP_GUID_LABEL, locale));
        pspGuid.setDescription(this.i18n.getMessage(PSP_GUID_DESCRIPTION, locale));
        pspGuid.setRequired(true);
        parameters.add(pspGuid);

        // api Marketing
        final PasswordParameter apiMarketing = new PasswordParameter();
        apiMarketing.setKey(API_MARKETING_KEY);
        apiMarketing.setLabel(this.i18n.getMessage(API_MARKETING_LABEL, locale));
        apiMarketing.setDescription(this.i18n.getMessage(API_MARKETING_DESCRIPTION, locale));
        apiMarketing.setRequired(true);
        parameters.add(apiMarketing);

        // OPC
        final InputParameter opc = new InputParameter();
        opc.setKey(OPC_KEY);
        opc.setLabel(this.i18n.getMessage(OPC_LABEL, locale));
        opc.setDescription(this.i18n.getMessage(OPC_DESCRIPTION, locale));
        opc.setRequired(true);
        parameters.add(opc);

        // NB Echeances
        final ListBoxParameter nbEcheancesParameter = new ListBoxParameter();
        nbEcheancesParameter.setKey(NB_ECHEANCES_KEY);
        nbEcheancesParameter.setLabel(this.i18n.getMessage(NB_ECHEANCES_LABEL, locale));
        nbEcheancesParameter.setRequired(true);
        final LinkedHashMap<String, String> nbEcheances = new LinkedHashMap<>();
        nbEcheances.put("3", "3");
        nbEcheances.put("4", "4");
        nbEcheancesParameter.setList(nbEcheances);
        parameters.add(nbEcheancesParameter);

        // X-Oney-Partner-Country-Code ISO 3166-1 alpha-2
        final InputParameter codePays = new InputParameter();
        codePays.setKey(COUNTRY_CODE_KEY);
        codePays.setLabel(this.i18n.getMessage(COUNTRY_CODE_LABEL, locale));
        codePays.setDescription(COUNTRY_CODE_DESCRIPTION);
        codePays.setRequired(true);
        parameters.add(codePays);

        //merchant_language_code ISO 639-1
        final InputParameter merchantLanguageCode = new InputParameter();
        merchantLanguageCode.setKey(LANGUAGE_CODE_KEY);
        merchantLanguageCode.setLabel(this.i18n.getMessage(LANGUAGE_CODE_LABEL, locale));
        merchantLanguageCode.setDescription(this.i18n.getMessage(LANGUAGE_CODE_DESCRIPTION, locale));
        merchantLanguageCode.setRequired(false);
        parameters.add(merchantLanguageCode);

        //X-Oney-International-Partner-ID
        final InputParameter internationalPartnerId = new InputParameter();
        internationalPartnerId.setKey(ID_INTERNATIONAL_KEY);
        internationalPartnerId.setLabel(this.i18n.getMessage(ID_INTERNATIONAL_LABEL, locale));
        internationalPartnerId.setDescription(ID_INTERNATIONAL_DESCRIPTION);
        internationalPartnerId.setRequired(false);
        parameters.add(internationalPartnerId);


        return parameters;

    }

    @Override
    public Map<String, String> check(ContractParametersCheckRequest contractParametersCheckRequest) {

        Locale locale = contractParametersCheckRequest.getLocale();
        final Map<String, String> errors = new HashMap<>();
        PartnerConfiguration partnerConfiguration = contractParametersCheckRequest.getPartnerConfiguration();
        ContractConfiguration contractConfiguration = contractParametersCheckRequest.getContractConfiguration();

        // psp id
        final String pspId = partnerConfiguration.getProperty(PSP_GUID_KEY);
        if (PluginUtils.isEmpty(pspId)) {
            errors.put(PSP_GUID_KEY, this.i18n.getMessage(PSP_GUID_MESSAGE_ERROR, locale));
        }

        final String key = contractParametersCheckRequest.getPartnerConfiguration().getProperty(PARTNER_CHIFFREMENT_KEY);
        if (PluginUtils.isEmpty(key)) {
            errors.put(PARTNER_CHIFFREMENT_KEY, this.i18n.getMessage(PARTNER_CHIFFREMENT_KEY_MESSAGE_ERROR, locale));
        }

        // apiKey
        final String partnerKey = contractParametersCheckRequest.getPartnerConfiguration().getProperty(PARTNER_AUTHRIZATION_KEY);
        if (PluginUtils.isEmpty(partnerKey)) {
            errors.put(PARTNER_AUTHRIZATION_KEY, this.i18n.getMessage(PARTNER_AUTHRIZATION_KEY_MESSAGE_ERROR, locale));
        }

        // merchant guid
        final ContractProperty merchantGuid = contractConfiguration.getProperty(MERCHANT_GUID_KEY);
        if (PluginUtils.isEmpty(merchantGuid)) {
            errors.put(MERCHANT_GUID_KEY, this.i18n.getMessage(MERCHANT_GUID_MESSAGE_ERROR, locale));
        }

        // OPC
        final ContractProperty opcKey = contractConfiguration.getProperty(OPC_KEY);
        if (PluginUtils.isEmpty(opcKey)) {
            errors.put(OPC_KEY, this.i18n.getMessage(OPC_MESSAGE_ERROR, locale));
        }

        final ContractProperty merchantLanguageCode = contractParametersCheckRequest.getContractConfiguration().getProperty(LANGUAGE_CODE_KEY);
        if (!PluginUtils.isISO639(merchantLanguageCode)) {
            errors.put(LANGUAGE_CODE_KEY, this.i18n.getMessage(LANGUAGE_NOT_ISO, locale));
        }

        final ContractProperty codePays = contractParametersCheckRequest.getContractConfiguration().getProperty(COUNTRY_CODE_KEY);
        if (!PluginUtils.isISO3166(codePays)) {
            errors.put(COUNTRY_CODE_KEY, this.i18n.getMessage(COUNTRY_NOT_ISO, locale));
        }


        if (!errors.isEmpty()) {
            return errors;
        }

        try {

            String jsonMsg = getFinalJsonMessage(pspId, merchantGuid, opcKey);
            Map<String, String> parameters = PluginUtils.getParametersMap(partnerConfiguration, codePays.getValue());
            OneyEncryptedRequest requestEncrypted = OneyEncryptedRequest.fromJson(jsonMsg, contractParametersCheckRequest);
            StringResponse stringResponse = httpClient.initiateCheckPayment(requestEncrypted.toString(), parameters);
            if (stringResponse == null) {
                errors.put(PARTNER_API_URL, UNEXPECTED_ERR);
                LOGGER.error("HTTP response is not parsable");
            } else {
                OneyError40x err = null;
                PaymentErrorResponse paymentErrorResponse = paymentErrorResponseFromJson(stringResponse.getContent());

                switch (stringResponse.getCode()) {
                    case HTTP_401:
                        err = OneyError40x.parseJson(stringResponse.getContent());
                        String errMsg = err.getPrintableMessage();
                        LOGGER.error(errMsg);
                        if (errMsg.contains("invalid subscription key")) {
                            errors.put(PARTNER_AUTHRIZATION_KEY, err.getMessage());
                        } else {
                            LOGGER.error("Les paramètres {} et {} ne correspondent pas", PARTNER_AUTHRIZATION_KEY, COUNTRY_CODE_KEY);
                            errors.put(COUNTRY_CODE_KEY, err.getMessage());

                        }
                        break;
                    case HTTP_400:
                        err = OneyError40x.parseJson(stringResponse.getContent());
                        LOGGER.error("Paramètre {} incorrect (non supporté par Oney) {}", COUNTRY_CODE_KEY, err.getMessage());
                        if (err.getMessage().contains("X-Oney-Partner-Country-Code")) {
                            errors.put(COUNTRY_CODE_KEY, err.getMessage());
                        }
                        break;
                    case HTTP_500:
                        OneyError oneyError = null;
                        List<OneyError> oneyErrors = paymentErrorResponse.getErrorList();
                        if (oneyErrors == null || oneyErrors.isEmpty() || oneyErrors.get(0) == null) {
                            LOGGER.error("Oney error is not parsable");
                            errors.put(PARTNER_API_URL, UNEXPECTED_ERR);
                            break;
                        }

                        oneyError = oneyErrors.get(0);

                        if (oneyError.getErrorMessge().startsWith("FindBounds: one of the values (-1.000000, 3.121696) cannot be used")) {
                            LOGGER.info("Fin de vérification des paramètres, aucune annomalie détectée");
                        } else {
                            errors.put(OPC_KEY, oneyError.getErrorMessge());
                        }
                        break;
                    default:
                        break;
                }

            }

        } catch (Exception e) {
            if (e instanceof DecryptException) {
                LOGGER.error("URL call throws an IOException", e);
                errors.put(PARTNER_CHIFFREMENT_KEY, e.getMessage());
            } else if (e instanceof IOException) {
                LOGGER.error("URL call throws an IOException", e);
                errors.put(PARTNER_API_URL, e.getMessage());
            } else {
                LOGGER.error("HTTP response is not parsable");
                errors.put(PARTNER_API_URL, UNEXPECTED_ERR);
            }
        }
        return errors;
    }

    private String getFinalJsonMessage(String pspId, ContractProperty merchantGuid, ContractProperty opcKey) {
        return TEST_JSON_MSG.replace(PSP_GUID_TAG, pspId)
                .replace(MERCHANT_GUID_TAG, merchantGuid.getValue())
                .replace(OPC_KEY_TAG, opcKey.getValue());
    }

    @Override
    public ReleaseInformation getReleaseInformation() {

        LocalDate date = LocalDate.parse(ReleasePropertiesEnum.INSTANCE.get(ConfigurationConstants.RELEASE_DATE),
                DateTimeFormatter.ofPattern(ConfigurationConstants.RELEASE_DATE_FORMAT));
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate(date)
                .withVersion(ReleasePropertiesEnum.INSTANCE.get(ConfigurationConstants.RELEASE_VERSION))
                .build();
    }

    @Override
    public String getName(Locale locale) {

        return this.i18n.getMessage(ConfigurationConstants.PAYMENT_METHOD_NAME, locale);
    }
}
