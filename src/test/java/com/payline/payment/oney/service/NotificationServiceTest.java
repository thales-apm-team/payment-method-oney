package com.payline.payment.oney.service;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.NotificationServiceImpl;
import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class NotificationServiceTest {

    public NotificationServiceImpl service;


    //todo Mock theses calls
    @Mock
    private OneyHttpClient httpClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void initiateGetTransactionStatusTest() throws DecryptException, IOException, URISyntaxException {

        StringResponse responseMockedKO = createStringResponse(404,"ZZRessource Not Found","{ \"statusCode\": 404, \"message\": \"Resource not found\" }" );

        StringResponse responseMockedOK = createStringResponse(200,"OK","{\"content\":\"{\\\"encrypted_message\\\":\\\"+l2i0o7hGRh+wJO02++ul41+5xLG5BBT+jV4I19n1BxNgTTBkgClTslC3pM/0UXrEOJt3Nv3LTMrGFG1pzsOP6gxM5c+lw57K0YUbQqoGgI\\u003d\\\"}\",\"code\":200,\"message\":\"OK\"}" );

        Mockito.doReturn(responseMockedOK).when(httpClient).doGet(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyMap(),Mockito.anyBoolean());

        OneyTransactionStatusRequest request = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .withLanguageCode("FR")
                .withMerchantGuid("9813e3ff-c365-43f2-8dca-94b850befbf9")
                .withPspGuid("6ba2a5e2-df17-4ad7-8406-6a9fc488a60a")
                .withPurchaseReference("CMDE|transactionID1544809131873")
                .build();

        this.httpClient = OneyHttpClient.getInstance();
        StringResponse transactStatus = this.httpClient.initiateGetTransactionStatus(request, true);
        System.out.println(transactStatus);
        Assert.assertNotNull(transactStatus.getCode());
        //decode Encrypt message
    }
}
