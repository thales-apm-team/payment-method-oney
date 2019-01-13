package com.payline.payment.oney.request;

import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createCompleteRedirectionPaymentBuilder;

public class OneyConfirmRequestTest {

    private OneyConfirmRequest paymentRequest;

    @Test
    public void buildOneyConfirmRequest() throws InvalidRequestException {

        paymentRequest = new OneyConfirmRequest.Builder(createCompleteRedirectionPaymentBuilder())
                .build();

        Assertions.assertTrue(paymentRequest.toString().contains("merchant_guid"));
        Assertions.assertTrue(paymentRequest.toString().contains("psp_guid"));
        Assertions.assertTrue(paymentRequest.toString().contains("language_code"));
        Assertions.assertTrue(paymentRequest.toString().contains("merchant_request_id"));
        Assertions.assertTrue(paymentRequest.toString().contains("payment"));
    }

}
