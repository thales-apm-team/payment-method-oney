package com.payline.payment.oney.request;

import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.payment.oney.service.impl.request.OneyPaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createCompletePaymentBuilder;

public class OneyPaymentRequestTest {

    private OneyPaymentRequest paymentRequest;

    @Test
    public void buildOneyPaymentRequest() throws InvalidRequestException {
        paymentRequest = OneyPaymentRequest.Builder.aOneyPaymentRequest()
                .fromPaylineRequest(createCompletePaymentBuilder().build())
                .build();

        Assertions.assertTrue(paymentRequest.toString().contains("merchant_guid"));
        Assertions.assertTrue(paymentRequest.toString().contains("psp_guid"));
        Assertions.assertTrue(paymentRequest.toString().contains("language_code"));
        Assertions.assertTrue(paymentRequest.toString().contains("merchant_request_id"));
        Assertions.assertTrue(paymentRequest.toString().contains("purchase"));
        Assertions.assertTrue(paymentRequest.toString().contains("customer"));
        Assertions.assertTrue(paymentRequest.toString().contains("payment"));
    }
}



