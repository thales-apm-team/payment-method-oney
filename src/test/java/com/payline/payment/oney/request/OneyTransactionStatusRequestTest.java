package com.payline.payment.oney.request;

import com.payline.payment.oney.bean.request.OneyTransactionStatusRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OneyTransactionStatusRequestTest {

    @Test
    public void testBuildRequest() {
        OneyTransactionStatusRequest request = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .withLanguageCode("FR")
                .withMerchantGuid("guid")
                .withPspGuid("pspG")
                .withPurchaseReference("maCommande")
                .withEncryptKey("66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=")
                .build();

        Assertions.assertEquals("FR", request.getLanguageCode());
        Assertions.assertEquals("guid", request.getMerchantGuid());
        Assertions.assertEquals("maCommande", request.getPurchaseReference());
    }
}
