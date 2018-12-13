package com.payline.payment.oney.service;

import com.payline.payment.oney.InvalidRequestException;
import com.payline.payment.oney.service.impl.PaymentServiceImpl;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseRedirect;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.oney.utils.TestUtils.createCompletePaymentBuilder;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class PaymentServiceImplTest {


    @Rule
   public ExpectedException expectedEx = ExpectedException.none();

    @Spy
    OneyHttpClient httpClient;

    @InjectMocks
    PaymentServiceImpl service;


    @Before
    public void setup() {
        service = new PaymentServiceImpl();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void paymentRequestOK() throws InvalidRequestException, IOException, URISyntaxException {

        StringResponse responseEncryptedMocked = createStringResponse(200,"","{\"encrypted_message\": \"FhzjXBU2Ek+/dmCMVB4wWn6ytL2+dh5mIx+gxDtcp4rTSzO/LA1Q72aClEvNoeXVdc3wg8L8PpMvAhRkWkLc1DyuX14icAZP8C7uA5COgRIzklUPJq/d9tiDWXxszS9o4ALbCfpGYqSgUN38fBnJhC9Y7RBqY4eq+H0iTRtvfYSLmKumsYvQFJY/21j+Xou/ZLppruwA6/MNC0nDGXw2o2PJeMGm+e5i4lUlqowvecmZ+GWQM91pOrb95B/pqriDYwZnnRQrewuhAyvIkR8LVQ==\"}");
        Mockito.doReturn(responseEncryptedMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString());

        PaymentResponseRedirect response = (PaymentResponseRedirect) service.paymentRequest(createCompletePaymentBuilder().build());
        Assert.assertNotNull(response.getRedirectionRequest().getUrl());
        Assert.assertNotNull(response.getRequestContext().getRequestData().get(OneyConstants.MERCHANT_GUID_KEY));

    }

    @Test
    public void paymentRequestKO() throws InvalidRequestException, IOException, URISyntaxException {
        StringResponse responseMocked = createStringResponse(400,"Bad request", "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"purchase.delivery.delivery_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"customer.customer_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"payment.payment_type\",\"error_code\":\"ERR_03\",\"error_label\":\"Format of the field is invalid [{Integer}]\"}]}}");
        Mockito.doReturn(responseMocked).when(httpClient).doPost(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString());
        PaymentResponseFailure response = (PaymentResponseFailure) service.paymentRequest(createCompletePaymentBuilder().build());
        Assert.assertNotNull(response);
        Assert.assertEquals("400", response.getErrorCode());
        Assert.assertEquals(FailureCause.PARTNER_UNKNOWN_ERROR, response.getFailureCause());

    }


}
