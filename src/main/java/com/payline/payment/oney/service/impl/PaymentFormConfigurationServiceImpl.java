package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.utils.i18n.I18nService;
import com.payline.payment.oney.utils.properties.constants.ConfigurationConstants;
import com.payline.payment.oney.utils.properties.service.LogoPropertiesEnum;
import com.payline.pmapi.bean.paymentform.bean.PaymentFormLogo;
import com.payline.pmapi.bean.paymentform.bean.form.NoFieldForm;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseSpecific;
import com.payline.pmapi.bean.paymentform.response.logo.PaymentFormLogoResponse;
import com.payline.pmapi.bean.paymentform.response.logo.impl.PaymentFormLogoResponseFile;
import com.payline.pmapi.logger.LogManager;
import com.payline.pmapi.service.PaymentFormConfigurationService;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;

import static com.payline.payment.oney.utils.OneyConstants.NB_ECHEANCES_KEY;
import static com.payline.payment.oney.utils.properties.constants.LogoConstants.*;

public class PaymentFormConfigurationServiceImpl implements PaymentFormConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentFormConfigurationServiceImpl.class);
    private I18nService i18n = I18nService.getInstance();

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration(PaymentFormConfigurationRequest paymentFormConfigurationRequest) {

        Locale locale = paymentFormConfigurationRequest.getLocale();
        String nbEcheances = null;
        try {
            nbEcheances = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentFormConfigurationRequest, NB_ECHEANCES_KEY);
        } catch (InvalidDataException e) {
            LOGGER.error("Failed to get the following contract property : nbEcheances");
            return e.toPaymentFormConfigurationResponseFailure();
        }

        final NoFieldForm noFieldForm = NoFieldForm
                .NoFieldFormBuilder
                .aNoFieldForm()
                .withDisplayButton(true)
                .withButtonText(buildLabel(ConfigurationConstants.PAYMENT_BUTTON_TEXT, locale, nbEcheances))
                .withDescription(buildLabel(ConfigurationConstants.PAYMENT_BUTTON_DESC, locale, nbEcheances))
                .build();

        return PaymentFormConfigurationResponseSpecific
                .PaymentFormConfigurationResponseSpecificBuilder
                .aPaymentFormConfigurationResponseSpecific()
                .withPaymentForm(noFieldForm)
                .build();
    }

    private String buildLabel(String key, Locale locale, String nbEcheances) {

        String base = this.i18n.getMessage(key, locale);
        return MessageFormat.format(base, nbEcheances);
    }

    @Override
    public PaymentFormLogoResponse getPaymentFormLogo(PaymentFormLogoRequest paymentFormLogoRequest) {

        Locale locale = paymentFormLogoRequest.getLocale();

        return PaymentFormLogoResponseFile.PaymentFormLogoResponseFileBuilder.aPaymentFormLogoResponseFile()
                .withHeight(Integer.valueOf(LogoPropertiesEnum.INSTANCE.get(LOGO_HEIGHT)))
                .withWidth(Integer.valueOf(LogoPropertiesEnum.INSTANCE.get(LOGO_WIDTH)))
                .withTitle(i18n.getMessage(ConfigurationConstants.PAYMENT_METHOD_NAME, locale))
                .withAlt(i18n.getMessage(ConfigurationConstants.PAYMENT_METHOD_NAME, locale))
                .build();

    }


    @Override
    public PaymentFormLogo getLogo(String var1, Locale locale) {

        String fileName = LogoPropertiesEnum.INSTANCE.get(LOGO_FILE_NAME);
        InputStream input = PaymentFormConfigurationServiceImpl.class.getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            LOGGER.error("Unable to load the logo {}", LOGO_FILE_NAME);
            throw new RuntimeException("Unable to load the logo " + LOGO_FILE_NAME);
        }
        try {
            // Read logo file
            BufferedImage logo = ImageIO.read(input);

            // Recover byte array from image
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(logo, LogoPropertiesEnum.INSTANCE.get(LOGO_FORMAT), baos);

            return PaymentFormLogo.PaymentFormLogoBuilder.aPaymentFormLogo()
                    .withFile(baos.toByteArray())
                    .withContentType(LogoPropertiesEnum.INSTANCE.get(LOGO_CONTENT_TYPE))
                    .build();
        } catch (IOException e) {
            LOGGER.error("Unable to load the logo", e);
            throw new RuntimeException(e);
        }
    }
}
