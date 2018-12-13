package com.payline.payment.oney.service.impl.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static com.payline.payment.oney.utils.OneyConstants.CHIFFREMENT_KEY;

public class OneySuccessPaymentResponse extends OneyResponse {

    @SerializedName("returned_url")
    private String returnedUrl;
    @SerializedName("encrypted_message")
    private String encryptedMessage;

    public String getReturnedUrl() {
        return returnedUrl;
    }
    public void setReturnedUrl(String url){
        this.returnedUrl = url;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }


    public static OneySuccessPaymentResponse paymentSuccessResponseFromJson (String json) {
        Gson parser = new Gson();
        OneySuccessPaymentResponse paymentSuccessResponse = parser.fromJson(json, OneySuccessPaymentResponse.class);

        //Cas reponse est chiffree : on dechiffre la reponse afin de recuperer l'url a renvoyer
        if(paymentSuccessResponse.getReturnedUrl() == null){
            String decryptedMessage = OneyResponse.decryptMessage(paymentSuccessResponse.getEncryptedMessage(), CHIFFREMENT_KEY);
//            String decryptedMessage = OneyResponse.decryptMessage(paymentSuccessResponse.getEncryptedMessage(), ConfigProperties.get(CHIFFREMENT_KEY));
            paymentSuccessResponse.setReturnedUrl(parser.fromJson(decryptedMessage, OneySuccessPaymentResponse.class).getReturnedUrl());
        }
        return paymentSuccessResponse;
    }

}
