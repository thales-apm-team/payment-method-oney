package com.payline.payment.oney.request;

import com.payline.payment.oney.service.impl.request.OneyRefundRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createDefaultRefundRequest;

public class OneyRefundRequestTest {

    @Test
    public void createRefundRequestOK(){
        OneyRefundRequest request = OneyRefundRequest.Builder.aOneyRefundRequest()
                .fromRefundRequest(createDefaultRefundRequest())
                .build();

        Assertions.assertNotNull(request);
        Assertions.assertNotNull(request.getPurchaseReference());
        Assertions.assertNotNull(request.getMerchantGuid());
        Assertions.assertTrue(request.toString().contains("merchant_request_id"));
        Assertions.assertTrue(request.toString().contains("purchase"));


    }
    @Test
    public void createRefundRequestKO(){
        //todo

    }

}
