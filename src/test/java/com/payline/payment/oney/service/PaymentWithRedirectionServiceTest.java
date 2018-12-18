package com.payline.payment.oney.service;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.oney.utils.TestUtils.createCompleteRedirectionPaymentBuilder;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class PaymentWithRedirectionServiceTest {

    @InjectMocks
    public PaymentWithRedirectionServiceImpl service;

    @Spy
    OneyHttpClient httpClient;

    @Before
    public void setup() {
        service = new PaymentWithRedirectionServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void confirmPaymentTest() throws IOException, URISyntaxException, DecryptException {
        StringResponse responseMocked = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ulzsMg0QfZ1N009CwI1PLZzBnbfv6/Enufe5TriN1gKQkEmbMYU0PMtHdk+eF7boW/lsIc5PmjpFX1E/4MUJGkzI=\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest(createCompleteRedirectionPaymentBuilder())
                .build();

        PaymentResponse response = service.validatePayment(paymentRequest, true);

        if (response.getClass() == PaymentResponseSuccess.class) {
            PaymentResponseSuccess success = (PaymentResponseSuccess) response;
            Assert.assertEquals("200", success.getStatusCode());
            Assert.assertEquals("Transaction is completed", success.getMessage().getMessage());
            Assert.assertNotNull(success.getTransactionAdditionalData());
        }

    }

    @Test
    public void confirmPaymentTestInvalidData() throws IOException, URISyntaxException, DecryptException {
        StringResponse responseMocked = createStringResponse(400, "Bad Request", "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"Merchant_request_id\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{String}]\"}]}}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest(createCompleteRedirectionPaymentBuilder())
                .build();

        PaymentResponse response = service.validatePayment(paymentRequest, true);
        PaymentResponseFailure fail = (PaymentResponseFailure) response;
        Assert.assertEquals("400", fail.getErrorCode());
        Assert.assertEquals(FailureCause.INVALID_DATA, fail.getFailureCause());
    }


    @Test
    public void confirmPaymentTestNotFound() throws IOException, URISyntaxException, DecryptException {
        StringResponse responseMocked = createStringResponse(404, "Bad request", "{\"content\":\"{ \"statusCode\": 404, \"message\": \"Resource not found\" }\",\"code\":404,\"message\":\"Resource Not Found\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest( createCompleteRedirectionPaymentBuilder())
                .build();

        PaymentResponseFailure response = (PaymentResponseFailure) service.validatePayment(paymentRequest, true);
        Assert.assertEquals("404", response.getErrorCode());
        Assert.assertEquals(FailureCause.COMMUNICATION_ERROR, response.getFailureCause());


    }

    @Test
    public void finalizeRedirectionPaymentTestOK() throws IOException, URISyntaxException {
        StringResponse responseMocked = createStringResponse(200, "OK", "{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ulzsMg0QfZ1N009CwI1PLZzBnbfv6/Enufe5TriN1gKQkEmbMYU0PMtHdk+eF7boW/lsIc5PmjpFX1E/4MUJGkzI=\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean());

        RedirectionPaymentRequest redirectionPaymentRequest = createCompleteRedirectionPaymentBuilder();
        PaymentResponse response = service.finalizeRedirectionPayment(redirectionPaymentRequest);

        Assert.assertEquals(PaymentResponseSuccess.class, response.getClass());
        PaymentResponseSuccess success = (PaymentResponseSuccess) response;
        Assert.assertNotNull(success.getPartnerTransactionId());
        Assert.assertNotNull(success.getMessage());
        Assert.assertNotNull(success.getStatusCode());
        Assert.assertNotNull(success.getTransactionDetails());
        Assert.assertEquals("Transaction is completed", success.getTransactionDetails());

    }

    @Test
    public void finalizeRedirectionPaymentTestKO() throws IOException, URISyntaxException {
        StringResponse responseMocked = createStringResponse(200, "OK", "{\"encrypted_message\":\"ymDHJ7HBRe49whKjH1HDtA==\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyBoolean());

        RedirectionPaymentRequest redirectionPaymentRequest = createCompleteRedirectionPaymentBuilder();
        PaymentResponse response = service.finalizeRedirectionPayment(redirectionPaymentRequest);

        Assert.assertEquals(PaymentResponseFailure.class, response.getClass());
        PaymentResponseFailure failure = (PaymentResponseFailure) response;
        Assert.assertNotNull(failure.getPartnerTransactionId());
        Assert.assertNotNull(failure.getFailureCause());
        Assert.assertEquals(FailureCause.REFUSED, failure.getFailureCause());


    }

}
