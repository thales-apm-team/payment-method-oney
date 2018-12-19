package com.payline.payment.oney.request;

import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
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
                .build();

        Assertions.assertEquals("FR", request.getLanguageCode());
        Assertions.assertEquals("guid", request.getMerchantGuid());
        Assertions.assertEquals("maCommande", request.getPurchaseReference());
    }
}
