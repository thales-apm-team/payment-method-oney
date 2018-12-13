package com.payline.payment.oney.service;

import com.payline.payment.oney.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.oney.utils.TestUtils.createCompleteRedirectionPaymentBuilder;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class PaymentWithRedirectionServiceTest {

    private PaymentWithRedirectionServiceImpl service;

    @Spy
    OneyHttpClient httpClient;

    @Before
    public void setup() {
        service = new PaymentWithRedirectionServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void confirmPaymentTest() throws IOException, URISyntaxException {
        StringResponse responseMocked = createStringResponse(200,"OK", "{\"content\":\"{ \"statusCode\": 404, \"message\": \"Resource not found\" }\",\"code\":404,\"message\":\"Resource Not Found\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest((RedirectionPaymentRequest) createCompleteRedirectionPaymentBuilder().build())
                .build();

        PaymentResponse response = service.validatePayment(paymentRequest,true);

        System.out.println(response);

        Assert.fail();

    }

    @Test
    public void confirmPaymentTestKO() throws IOException, URISyntaxException {
        StringResponse responseMocked = createStringResponse(404,"Bad request", "{\"content\":\"{ \"statusCode\": 404, \"message\": \"Resource not found\" }\",\"code\":404,\"message\":\"Resource Not Found\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest((RedirectionPaymentRequest) createCompleteRedirectionPaymentBuilder().build())
                .build();

        PaymentResponseFailure response = (PaymentResponseFailure)service.validatePayment(paymentRequest,true);

        Assert.assertEquals("404", response.getErrorCode() );
        Assert.assertEquals(FailureCause.COMMUNICATION_ERROR, response.getFailureCause() );


    }

}
