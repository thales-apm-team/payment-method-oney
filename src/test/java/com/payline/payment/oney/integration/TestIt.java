package com.payline.payment.oney.integration;

import com.payline.payment.oney.service.impl.PaymentServiceImpl;
import com.payline.payment.oney.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.payment.oney.utils.TestUtils;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.integration.AbstractPaymentIntegration;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.OneyConstants.ID_INTERNATIONAL_KEY;
import static com.payline.payment.oney.utils.OneyConstants.LANGUAGE_CODE_KEY;

public class TestIt extends AbstractPaymentIntegration {
    private PaymentServiceImpl paymentService = new PaymentServiceImpl();
    private PaymentWithRedirectionServiceImpl paymentWithRedirectionService = new PaymentWithRedirectionServiceImpl();

    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        HashMap <String, ContractProperty> contractConfiguration = new HashMap();
        contractConfiguration.put(BUSINESS_TRANSACTION_CODE, new ContractProperty("3x002"));
        contractConfiguration.put(MERCHANT_GUID_KEY, new ContractProperty("9813e3ff-c365-43f2-8dca-94b850befbf9"));
        contractConfiguration.put(PSP_GUID_KEY, new ContractProperty("6ba2a5e2-df17-4ad7-8406-6a9fc488a60a"));
        contractConfiguration.put(API_MARKETING_KEY, new ContractProperty("01c6ea9021574d608c631f1c3b880b3be"));
        contractConfiguration.put(OPC_KEY, new ContractProperty("3x002"));
        contractConfiguration.put(NB_ECHEANCES_KEY, new ContractProperty("3"));
        contractConfiguration.put(COUNTRY_CODE_KEY, new ContractProperty("FRA")); //3 caractères
        contractConfiguration.put(LANGUAGE_CODE_KEY, new ContractProperty("FR"));
        contractConfiguration.put(ID_INTERNATIONAL_KEY, new ContractProperty("FR"));
    return    contractConfiguration;
    }

    @Override
    protected PaymentFormContext generatePaymentFormContext() {
        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put(PSP_GUID_KEY,"6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        Map<String, String> sensitivePaymentFormParameter = new HashMap<>();
        return PaymentFormContext.PaymentFormContextBuilder
                .aPaymentFormContext()
                .withPaymentFormParameter(paymentFormParameter)
                .withSensitivePaymentFormParameter(sensitivePaymentFormParameter)
                .build();
    }

    @Override
    protected String payOnPartnerWebsite(String partnerUrl) {
        // Start browser
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Thales\\Documents\\HME\\moyens de paiement\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
//fixme Not working
            // Go to partner's website
            driver.get(partnerUrl);
//            driver.findElement(By.id("classicPin-addPinField")).sendKeys(Utils.PAYMENT_TOKEN);


            //Id du bouton nouveau client
            driver.findElement(By.xpath("//*[@id='wt1_wt7_Oney_Theme_wt158_block_wtMainContent_WebPatterns_wtStructure_block_wtColumn1_wtWithoutAccount_rb2']")).click();
//            driver.findElement(By.name("wt1_wt7$1105887523")).click();
            //champs date de naissance
            driver.findElement(By.xpath("//*[@id='wt1_wt7_Oney_Theme_wt158_block_wtMainContent_WebPatterns_wtStructure_block_wtColumn1_WebPatterns_wt20_block_wtColumn1_wtCustomerData_Dateofbirth']")).sendKeys("1993-04-01");

            //numero de CB
            driver.findElement(By.xpath("//*[@data-checkout='card-number']")).sendKeys("4543474002249996");

            //mois de expiration CB
            driver.findElement(By.xpath("//*[@data-checkout='expiry-month']")).sendKeys("06");
            //annne expiration de CB
            driver.findElement(By.xpath("//*[@data-checkout='expiry-year']")).sendKeys("25");
            //CVV CB
            driver.findElement(By.xpath("//*[@data-checkout='CVV']")).sendKeys("956");

            //Checkboxes a cocher
            List<WebElement> els = driver.findElements(By.xpath("//input[@type='checkbox']"));
            for ( WebElement el : els ) {
                if ( !el.isSelected() ) {
                    el.click();
                }
            }
            // validate payment
            driver.findElement(By.xpath("//*[@type='submit']")).click();
            // Wait for redirection to success or cancel url
            WebDriverWait wait = new WebDriverWait(driver, 60);
         //   wait.until(ExpectedConditions.or(ExpectedConditions.urlToBe(SUCCESS_URL), ExpectedConditions.urlToBe(CANCEL_URL)));
            return driver.getCurrentUrl();
        } finally {
            driver.quit();
        }


    }

    @Override
    protected String cancelOnPartnerWebsite(String s) {
        return null;
    }

    @Test
    public void fullPaymentTest() {
        PaymentRequest request = createDefaultPaymentRequest();
        this.fullRedirectionPayment(request, paymentService, paymentWithRedirectionService);
    }

    @Override
    public PaymentRequest createDefaultPaymentRequest() {
        return TestUtils.createCompletePaymentBuilder().build();

    }
}
