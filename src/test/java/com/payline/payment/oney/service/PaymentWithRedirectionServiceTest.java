package com.payline.payment.oney.service;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.PaymentWithRedirectionServiceImpl;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
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
        StringResponse responseMocked = createStringResponse(200, "OK", "{language_code:FR,purchase:{status_code:FAVORABLE,status_label:\"La demande de paiement est dans un etat favorable pour financement\",reason_code:FAVORABLE,reason_label:\"My label \"}}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(),Mockito.anyBoolean());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest((RedirectionPaymentRequest) createCompleteRedirectionPaymentBuilder().build())
                .build();

        PaymentResponseSuccess response = (PaymentResponseSuccess) service.validatePayment(paymentRequest, true);

        Assert.assertEquals("200", response.getStatusCode());
        Assert.assertEquals("La demande de paiement est dans un etat favorable pour financement", response.getMessage().getMessage());
        Assert.assertNotNull(response.getTransactionAdditionalData());
    }

    @Test
    public void confirmPaymentTestKO() throws IOException, URISyntaxException, DecryptException {
        StringResponse responseMocked = createStringResponse(404, "Bad request", "{\"content\":\"{ \"statusCode\": 404, \"message\": \"Resource not found\" }\",\"code\":404,\"message\":\"Resource Not Found\"}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(),Mockito.anyBoolean());

        OneyConfirmRequest paymentRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest((RedirectionPaymentRequest) createCompleteRedirectionPaymentBuilder().build())
                .build();

        PaymentResponseFailure response = (PaymentResponseFailure) service.validatePayment(paymentRequest, true);

        Assert.assertEquals("404", response.getErrorCode());
        Assert.assertEquals(FailureCause.COMMUNICATION_ERROR, response.getFailureCause());


    }

}
