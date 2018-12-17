package com.payline.payment.oney.response;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.response.TransactionStatusResponse;
import com.payline.payment.oney.utils.http.StringResponse;
import org.junit.Assert;
import org.junit.Test;

import static com.payline.payment.oney.service.impl.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class TransactionStatusResponseTest {

    @Test
    public void transactionStatusTest() throws DecryptException {
        String responseDecrypted = "{\"purchase\": { \"status_code\": \"PENDING\", \"status_label\": \"Waiting for customer validation\" }}";

        //Cas reponse dechiffree
        TransactionStatusResponse status1 = createTransactionStatusResponseFromJson(responseDecrypted);
        Assert.assertEquals("PENDING", status1.getStatusPurchase().getStatusCode());
        Assert.assertEquals("Waiting for customer validation", status1.getStatusPurchase().getStatusLabel());

    }
    @Test
    public void transactionStatusEncryptedTest() throws DecryptException {
        StringResponse encryptedResponse = createStringResponse(200, "OK", "{\"content\":\"{\\\"encrypted_message\\\":\\\"+l2i0o7hGRh+wJO02++ul41+5xLG5BBT+jV4I19n1BxNgTTBkgClTslC3pM/0UXrEOJt3Nv3LTMrGFG1pzsOP6gxM5c+lw57K0YUbQqoGgI\\u003d\\\"}\",\"code\":200,\"message\":\"OK\"}");

        //Cas reponse dechiffre
        TransactionStatusResponse status1 = createTransactionStatusResponseFromJson(encryptedResponse.getContent());
//        Assert.assertEquals("PENDING", status1.getStatusPurchase().getStatusCode());
//        Assert.assertEquals("Waiting for customer validation", status1.getStatusPurchase().getStatusLabel());
        //reponse vide
        Assert.assertNotNull(status1.getStatusPurchase());


    }






}
