package com.payline.payment.oney.request;

import com.payline.payment.oney.bean.common.NavigationData;
import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.payment.BusinessTransactionData;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.payment.oney.bean.common.purchase.Purchase;
import com.payline.payment.oney.bean.request.OneyPaymentRequest;
import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.payment.oney.service.BeanAssembleService;
import com.payline.payment.oney.service.impl.BeanAssemblerServiceImpl;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.TestUtils;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.payline.payment.oney.utils.OneyConstants.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OneyPaymentRequestTest {

    private OneyPaymentRequest oneyPaymentRequest;

    private BeanAssembleService beanAssembleService;

    private PaymentRequest paymentRequest;

    @BeforeAll
    public void setUp() {
        beanAssembleService = BeanAssemblerServiceImpl.getInstance();
        paymentRequest = TestUtils.createDefaultPaymentRequest();
    }


    @Test
    public void buildOneyPaymentRequest() throws InvalidRequestException {

        final String merchGuid = paymentRequest.getContractConfiguration().getProperty(MERCHANT_GUID_KEY).getValue();
        final String language = paymentRequest.getLocale().getLanguage();
        final String merchantRequestId = PluginUtils.generateMerchantRequestId(merchGuid);
        final String pspGuid = paymentRequest.getPartnerConfiguration().getProperty(PSP_GUID_KEY);
        final String chiffrementKey = paymentRequest.getPartnerConfiguration().getProperty(CHIFFREMENT_KEY);
        final BusinessTransactionData businessTransaction = beanAssembleService.assembleBuisnessTransactionData(paymentRequest);
        final PaymentData paymentData = beanAssembleService.assemblePaymentData(paymentRequest, businessTransaction);
        final NavigationData navigationData = beanAssembleService.assembleNavigationData(paymentRequest);
        final Customer customer = beanAssembleService.assembleCustomer(paymentRequest);
        final Purchase purchase = beanAssembleService.assemblePurchase(paymentRequest);

        oneyPaymentRequest = OneyPaymentRequest.Builder.aOneyPaymentRequest()
                .withLanguageCode(language)
                .withMerchantRequestId(merchantRequestId)
                .withPspGuid(pspGuid)
                .withMerchantGuid(merchGuid)
                .withNavigation(navigationData)
                .withPaymentdata(paymentData)
                .withCustomer(customer)
                .withPurchase(purchase)
                .withMerchantLanguageCode(language)
                .withEncryptKey(chiffrementKey)
                .withMerchantContext(paymentRequest.getSoftDescriptor())
                .withPspContext(paymentRequest.getTransactionId())
                .build();
//        paymentRequest = OneyPaymentRequest.Builder.aOneyPaymentRequest()
//                .fromPaylineRequest(createCompletePaymentBuilder().build())
//                .build();

        String result = oneyPaymentRequest.toString();
        Assertions.assertTrue(result.contains("merchant_guid"));
        Assertions.assertTrue(result.contains("psp_guid"));
        Assertions.assertTrue(result.contains("language_code"));
        Assertions.assertTrue(result.contains("merchant_request_id"));
        Assertions.assertTrue(result.contains("purchase"));
        Assertions.assertTrue(result.contains("customer"));
        Assertions.assertTrue(result.contains("payment"));
    }
}



