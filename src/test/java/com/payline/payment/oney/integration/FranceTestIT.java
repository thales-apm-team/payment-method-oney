package com.payline.payment.oney.integration;

import com.payline.payment.oney.utils.TestCountry;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.payline.payment.oney.utils.OneyConstants.*;

public class FranceTestIT extends TestIT {

    @Override
    public TestCountry getCountry() {
        return TestCountry.FR;
    }

    @Override
    public Map.Entry<String, String> getPspGuid() {
        return new AbstractMap.SimpleEntry<>(PSP_GUID_KEY + ".fr", "34b7cd77fe6642a1acd9d91df5f1d1f0");
    }

    @Override
    public Map.Entry<String, String> getPartnerAuthorizationKey() {
        return new AbstractMap.SimpleEntry<>(PARTNER_AUTHORIZATION_KEY + ".fr", "06074f792da24b2e99cd9af3874aaf9f");
    }

    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        HashMap<String, ContractProperty> contractConfiguration = new HashMap();
        contractConfiguration.put(OPC_KEY, new ContractProperty("W4026"));
        contractConfiguration.put(MERCHANT_GUID_KEY, new ContractProperty("6fd0d7f8123b4a729cb74a89f32e6035"));
        contractConfiguration.put(NB_ECHEANCES_KEY, new ContractProperty("3"));
        contractConfiguration.put(COUNTRY_CODE_KEY, new ContractProperty("FR")); // caractères
        contractConfiguration.put(LANGUAGE_CODE_KEY, new ContractProperty("FR"));
        contractConfiguration.put(PARTNER_CHIFFREMENT_KEY, new ContractProperty("dqcoCKNBvHFIIvDlcsrqYg44Q2JhmjTmDJR6NwdBogU="));
        return contractConfiguration;
    }

    @Test
    public void fullPaymentTest() {
        PaymentRequest request = createDefaultPaymentRequest();
        this.fullRedirectionPayment(request, paymentService, paymentWithRedirectionService);
    }

    @Override
    protected String payOnPartnerWebsite(String partnerUrl) {
        // Start browser
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        try {
            // Go to partner's website
            driver.get(partnerUrl);

            // Wait for MANUAL filling and submission of the form...
            WebDriverWait wait = new WebDriverWait(driver, 120);
            wait.until(ExpectedConditions.or(ExpectedConditions.urlToBe(SUCCESS_URL), ExpectedConditions.urlToBe(CANCEL_URL)));

            return driver.getCurrentUrl();
        }
        finally {
            driver.quit();
        }
    }
}
