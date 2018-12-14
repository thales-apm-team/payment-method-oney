package com.payline.payment.oney.request;

import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
import org.junit.Assert;
import org.junit.Test;

public class OneyTransactionStatusRequestTest {

    @Test
    public void testBuildRequest(){
        OneyTransactionStatusRequest request = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .withLanguageCode("FR")
                .withMerchantGuid("guid")
                .withPspGuid("pspG")
                .withPurchaseReference("maCommande")
                .build();

        Assert.assertEquals("FR",request.getLanguageCode());
        Assert.assertEquals("guid",request.getMerchantGuid());
        Assert.assertEquals("maCommande",request.getPurchaseReference());
    }
}
