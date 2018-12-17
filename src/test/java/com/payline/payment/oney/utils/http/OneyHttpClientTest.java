package com.payline.payment.oney.utils.http;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class OneyHttpClientTest {


    //ToDO  mock http call, not mocked now to check if they work
    @Spy
    OneyHttpClient client;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doGet() throws IOException, URISyntaxException {
        this.client = OneyHttpClient.getInstance();
        String credentials = "7fd3f1c53b9a47f7b85c801a32971895";
        Map<String, String> param = new HashMap<>();
        param.put("psp_guid", "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        param.put("merchant_guid", "9813e3ff-c365-43f2-8dca-94b850befbf9");
        param.put("reference", "CMDE|455454545415451198a");

        StringResponse response = this.client.doGet("https", "oney-staging.azure-api.net", "/staging/payments/v1/purchase/", param,true);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());

    }

    @Test
    public void doPost() throws IOException, URISyntaxException {

        this.client = OneyHttpClient.getInstance();
        String credentials = "7fd3f1c53b9a47f7b85c801a32971895";
        String requestContent = HttpDataUtils.CREATE_REQ_BODY;

        StringResponse response = this.client.doPost("https", "oney-staging.azure-api.net", "/staging/payments/v1/purchase/facilypay_url", requestContent,true);

        System.out.println(response);
        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getCode());

    }

    @Test
    public void buildGetOrderPath() {

        this.client = OneyHttpClient.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("psp_guid", "val1");
        param.put("merchant_guid", "val2");
        param.put("reference", "val3");

        String pathAttempted = "somePath/psp_guid/val1/merchant_guid/val2/reference/val3";
        String path = client.buildGetOrderPath("somePath/", param);
        Assert.assertEquals(pathAttempted, path);
    }

    @Test
    public void buildConfirmOrderPath() {

        this.client = OneyHttpClient.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("psp_guid", "val1");
        param.put("merchant_guid", "val2");
        param.put("reference", "val3");

        String pathAttempted = "somePath/psp_guid/val1/merchant_guid/val2/reference/val3/action/confirm";
        String path = this.client.buildConfirmOrderPath("somePath/", param);
        Assert.assertEquals(pathAttempted, path);
    }

    @Test
    public void initiateGetTransactionStatusTest() throws DecryptException, IOException, URISyntaxException {

        StringResponse responseMockedOK = createStringResponse(200,"ZZOK","{\"content\":\"{\\\"encrypted_message\\\":\\\"+l2i0o7hGRh+wJO02++ul41+5xLG5BBT+jV4I19n1BxNgTTBkgClTslC3pM/0UXrEOJt3Nv3LTMrGFG1pzsOP6gxM5c+lw57K0YUbQqoGgI\\u003d\\\"}\",\"code\":200,\"message\":\"OK\"}" );
        Mockito.doReturn(responseMockedOK).when(client).doGet(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.any(Map.class),Mockito.anyBoolean());

        OneyTransactionStatusRequest request = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .withLanguageCode("FR")
                .withMerchantGuid("9813e3ff-c365-43f2-8dca-94b850befbf9")
                .withPspGuid("6ba2a5e2-df17-4ad7-8406-6a9fc488a60a")
                .withPurchaseReference("CMDE|455454545415451198114")
                .build();

        StringResponse transactStatus = this.client.initiateGetTransactionStatus(request, true);
        Assert.assertNotNull(transactStatus.getCode());
    }



}
