package com.payline.payment.oney.utils.http;

import org.apache.http.Header;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class OneyHttpClientTest {

    private OneyHttpClient client;
    //ToDO  mock http call, not mocked now to check if they work


    @Test
    public void doGet() throws IOException, URISyntaxException {
        this.client = OneyHttpClient.getInstance();
        String credentials ="7fd3f1c53b9a47f7b85c801a32971895";
        Map<String, String> param = new HashMap<>();
        param.put("psp_guid", "6ba2a5e2-df17-4ad7-8406-6a9fc488a60a");
        param.put("merchant_guid", "9813e3ff-c365-43f2-8dca-94b850befbf9");
        param.put("reference", "CMDE|455454545415451198a");

        StringResponse response = this.client.doGet("https", "oney-staging.azure-api.net", "/staging/payments/v1/purchase/", param);

        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());

    }

    @Test
    public void doPost() throws IOException, URISyntaxException {

        this.client = OneyHttpClient.getInstance();
        String credentials ="7fd3f1c53b9a47f7b85c801a32971895";
        String requestContent = HttpDataUtils.CREATE_REQ_BODY;

        StringResponse response = this.client.doPost("https", "oney-staging.azure-api.net", "/staging/payments/v1/purchase/facilypay_url", requestContent);

        System.out.println(response);
        //Assert we have a response
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getCode());

    }
    @Test
    public void buildGetOrderPath(){

        this.client = OneyHttpClient.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("psp_guid", "val1");
        param.put("merchant_guid", "val2");
        param.put("reference", "val3");

        String pathAttempted = "somePath/psp_guid/val1/merchant_guid/val2/reference/val3" ;
        String path = client.buildGetOrderPath("somePath/",param);
        Assert.assertEquals(pathAttempted,path);
    }

    @Test
    public void buildConfirmOrderPath(){

        this.client = OneyHttpClient.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("psp_guid", "val1");
        param.put("merchant_guid", "val2");
        param.put("reference", "val3");

        String pathAttempted = "somePath/psp_guid/val1/merchant_guid/val2/reference/val3/action/confirm" ;
        String path = client.buildConfirmOrderPath("somePath/",param);
        Assert.assertEquals(pathAttempted,path);
    }
}
