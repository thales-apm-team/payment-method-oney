package com.payline.payment.oney.request;

import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.payment.oney.service.impl.request.OneyPaymentRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.TestUtils.createCompletePaymentBuilder;

public class OneyPaymentRequestTest {

    private OneyPaymentRequest paymentRequest;

    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void buildOneyPaymentRequest() throws InvalidRequestException {
        paymentRequest = OneyPaymentRequest.Builder.aOneyPaymentRequest()
                .fromPaylineRequest(createCompletePaymentBuilder().build())
                .build();

        Assert.assertTrue(paymentRequest.toString().contains("merchant_guid"));
        Assert.assertTrue(paymentRequest.toString().contains("psp_guid"));
        Assert.assertTrue(paymentRequest.toString().contains("language_code"));
        Assert.assertTrue(paymentRequest.toString().contains("merchant_request_id"));
        Assert.assertTrue(paymentRequest.toString().contains("purchase"));
        Assert.assertTrue(paymentRequest.toString().contains("customer"));
        Assert.assertTrue(paymentRequest.toString().contains("payment"));
    }
}



