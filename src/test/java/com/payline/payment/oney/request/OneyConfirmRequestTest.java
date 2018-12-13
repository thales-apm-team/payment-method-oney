package com.payline.payment.oney.request;

import com.payline.payment.oney.InvalidRequestException;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import org.junit.Assert;
import org.junit.Test;

import static com.payline.payment.oney.utils.TestUtils.createCompleteRedirectionPaymentBuilder;

public class OneyConfirmRequestTest {

    private OneyConfirmRequest paymentRequest;

    @Test
    public void buildOneyConfirmRequest() throws InvalidRequestException {

        paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest((RedirectionPaymentRequest) createCompleteRedirectionPaymentBuilder().build())
        .build();

        Assert.assertTrue(paymentRequest.toString().contains("merchant_guid"));
        Assert.assertTrue(paymentRequest.toString().contains("psp_guid"));
        Assert.assertTrue(paymentRequest.toString().contains("language_code"));
        Assert.assertTrue(paymentRequest.toString().contains("merchant_request_id"));
        Assert.assertTrue(paymentRequest.toString().contains("payment"));
        System.out.println(paymentRequest);
    }

}
