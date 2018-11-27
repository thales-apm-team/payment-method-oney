package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.i18n.I18nService;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.InputParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.service.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.payline.payment.oney.utils.OneyConstants.*;

public class ConfigurationServiceImpl implements ConfigurationService {


    private static final Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);

    private I18nService i18n = I18nService.getInstance();
    //todo define  contract config we need to add

    @Override
    public List<AbstractParameter> getParameters(Locale locale) {
        List<AbstractParameter> parameters = new ArrayList<>();


        //Api Key?
        final InputParameter apiKey = new InputParameter();
        apiKey.setKey(X_ONEY_AUTHORIZATION_KEY);
        apiKey.setLabel(this.i18n.getMessage(X_ONEY_AUTHORIZATION_LABEL, locale));
        apiKey.setDescription(this.i18n.getMessage(X_ONEY_AUTHORIZATION_DESCRIPTION, locale));
        apiKey.setRequired(true);
        parameters.add(apiKey);

        // psp id
        final InputParameter pspId = new InputParameter();
        pspId.setKey(PSP_ID_KEY);
        pspId.setLabel(this.i18n.getMessage(PSP_ID_LABEL, locale));
        pspId.setDescription(this.i18n.getMessage(PSP_ID_DESCRIPTION, locale));
        pspId.setRequired(true);
        parameters.add(pspId);

        // psp id
        final InputParameter merchantGuid = new InputParameter();
        merchantGuid.setKey(MERCHANT_GUID_KEY);
        merchantGuid.setLabel(this.i18n.getMessage(MERCHANT_GUID_LABEL, locale));
        merchantGuid.setDescription(this.i18n.getMessage(MERCHANT_GUID_DESCRIPTION, locale));
        merchantGuid.setRequired(true);
        parameters.add(merchantGuid);

        return parameters;
    }

    @Override
    public Map<String, String> check(ContractParametersCheckRequest contractParametersCheckRequest) {

        Locale locale = contractParametersCheckRequest.getLocale();
        final Map<String, String> accountInfo = contractParametersCheckRequest.getAccountInfo();
        final Map<String, String> errors = new HashMap<>();

        // psp id
        final String apiKey = accountInfo.get(X_ONEY_AUTHORIZATION_KEY);
        if (PluginUtils.isEmpty(apiKey)) {
            errors.put(X_ONEY_AUTHORIZATION_KEY, this.i18n.getMessage(X_ONEY_AUTHORIZATION_MESSAGE_ERROR, locale));
        }

        // psp id
        final String pspId = accountInfo.get(PSP_ID_KEY);
        if (PluginUtils.isEmpty(pspId)) {
            errors.put(PSP_ID_KEY, this.i18n.getMessage(PSP_ID_MESSAGE_ERROR, locale));
        }

        // merchant guid
        final String merchantGuid = accountInfo.get(MERCHANT_GUID_KEY);
        if (PluginUtils.isEmpty(merchantGuid)) {
            errors.put(MERCHANT_GUID_KEY, this.i18n.getMessage(MERCHANT_GUID_MESSAGE_ERROR, locale));
        }
        return errors;
    }

    @Override
    public ReleaseInformation getReleaseInformation() {
        Properties props = new Properties();
        try {
            props.load(ConfigurationServiceImpl.class.getClassLoader().getResourceAsStream(RELEASE_PROPERTIES));
        } catch (IOException e) {
            LOGGER.error("An error occurred reading the file: {}" + RELEASE_PROPERTIES, e.getMessage(), e);
            throw new RuntimeException("Failed to reading file release.properties: ", e);

        }

        LocalDate date = LocalDate.parse(props.getProperty(RELEASE_DATE), DateTimeFormatter.ofPattern(RELEASE_DATE_FORMAT));
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate(date)
                .withVersion(props.getProperty(RELEASE_VERSION))
                .build();
    }

    @Override
    public String getName(Locale locale) {

        return this.i18n.getMessage(PAYMENT_METHOD_NAME, locale);
    }
}
