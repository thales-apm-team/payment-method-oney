package com.payline.payment.oney.service.impl.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;

import static com.payline.payment.oney.utils.OneyConstants.CHIFFREMENT_KEY;

public class OneyEncryptedRequest extends OneyBean {
    @SerializedName("merchant_guid")
    protected String  merchantGuid;
    @SerializedName("psp_guid")
    protected String  pspGuid;
    @SerializedName("encrypted_message")
    protected String  encryptedMessage;

    public String getMerchantGuid() {
        return merchantGuid;
    }

    public String getPspGuid() {
        return pspGuid;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

     public static OneyEncryptedRequest fromOneyPaymentRequest (OneyPaymentRequest request) {
         OneyEncryptedRequest encryptedRequest = new OneyEncryptedRequest();
//         encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), ConfigProperties.get(CHIFFREMENT_KEY));
         encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), CHIFFREMENT_KEY);
         encryptedRequest.pspGuid = request.pspGuid;
         encryptedRequest.merchantGuid =request.merchantGuid;

         return encryptedRequest;
     }

    public static OneyEncryptedRequest fromOneyConfirmRequest (OneyConfirmRequest request) {
        OneyEncryptedRequest encryptedRequest = new OneyEncryptedRequest();
//         encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), ConfigProperties.get(CHIFFREMENT_KEY));
        encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), CHIFFREMENT_KEY);
        encryptedRequest.pspGuid = request.pspGuid;
        encryptedRequest.merchantGuid =request.merchantGuid;

        return encryptedRequest;
    }
}
