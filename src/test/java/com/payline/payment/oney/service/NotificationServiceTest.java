package com.payline.payment.oney.service;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.NotificationServiceImpl;
import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.service.impl.response.TransactionStatusResponse;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static com.payline.payment.oney.service.impl.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class NotificationServiceTest {



    @Spy
     OneyHttpClient httpClient;

    @InjectMocks
     NotificationServiceImpl service;

    @Before
    public void setup() {
        service = new NotificationServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void notifyTransactionStatusRequestTest() throws DecryptException, IOException, URISyntaxException {

        StringResponse responseMockedPending = createStringResponse(200,"OK","{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ul3aakmok0anPtpBvW1vZ3e83c7evaIMgKsuqlJpPjg407AoMkFm94736cZcnpC81qiX4V8n9IxMD1E50QBAOkMZ1S8Pf90kxhXSDe3wt4J13\"}" );
        StringResponse responseMockedFavorable = createStringResponse(200,"OK","{\"encrypted_message\":\"+l2i0o7hGRh+wJO02++ul/bQBJ3C1/cyjmvmAAmMq9gLttO54jS+b/UB/MPwY6YeiFWc7TtYNuIHJF3Grkl2/O4B6r4zkTpus9DrEZIou4aE8tfX+G43n2zFDAoYG3u3\"}" );

        Mockito.doReturn(responseMockedPending).when(httpClient).doGet(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.any(Map.class),Mockito.anyBoolean());



        OneyTransactionStatusRequest request = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .withLanguageCode("FR")
                .withMerchantGuid("9813e3ff-c365-43f2-8dca-94b850befbf9")
                .withPspGuid("6ba2a5e2-df17-4ad7-8406-6a9fc488a60a")
                .withPurchaseReference("CMDE|455454545415451198119")
//                .withPurchaseReference("CMDE|455454545415451199121")
                .build();

        StringResponse transactStatus = this.httpClient.initiateGetTransactionStatus(request, true);
        TransactionStatusResponse resp = createTransactionStatusResponseFromJson(transactStatus.getContent());

        Assert.assertNotNull(transactStatus.getCode());
        Assert.assertEquals("Waiting for customer validation",resp.getStatusPurchase().getStatusLabel());
        Assert.assertEquals("PENDING",resp.getStatusPurchase().getStatusCode());

//        Cas Favorable
//        Assert.assertEquals("Oney accepts the payment",resp.getStatusPurchase().getStatusLabel());
//        Assert.assertEquals("FAVORABLE",resp.getStatusPurchase().getStatusCode());

    }
}
