package com.payline.payment.oney.bean.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.exception.HttpCallException;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

public class OneySuccessPaymentResponse extends OneyResponse {

    private static final Logger LOGGER = LogManager.getLogger(OneySuccessPaymentResponse.class);

    @SerializedName("returned_url")
    private String returnedUrl;


    public String getReturnedUrl() {
        return returnedUrl;
    }

    public URL getReturnedUrlAsUrl() throws HttpCallException {

        try {
            return new URL(returnedUrl);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new HttpCallException(e, "OneySuccessPaymentResponse.MalformedURLException");
        }
    }

    public void setReturnedUrl(String url) {
        this.returnedUrl = url;
    }


    public static OneySuccessPaymentResponse paymentSuccessResponseFromJson(String json, String encryptKey) throws DecryptException {
        Gson parser = new Gson();
        OneySuccessPaymentResponse paymentSuccessResponse = parser.fromJson(json, OneySuccessPaymentResponse.class);

        //Cas reponse est chiffree : on dechiffre la reponse afin de recuperer l'url a renvoyer
        if (paymentSuccessResponse.getReturnedUrl() == null) {
            String decryptedMessage = OneyResponse.decryptMessage(paymentSuccessResponse.getEncryptedMessage(), encryptKey);
            paymentSuccessResponse.setReturnedUrl(parser.fromJson(decryptedMessage, OneySuccessPaymentResponse.class).getReturnedUrl());
        }
        return paymentSuccessResponse;
    }

}
