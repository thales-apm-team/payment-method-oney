package com.payline.payment.oney.bean.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.payment.oney.exception.DecryptException;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;

import static com.payline.payment.oney.utils.OneyConstants.*;

public class OneyEncryptedRequest extends OneyBean {
    @SerializedName("merchant_guid")
    protected String merchantGuid;
    @SerializedName("psp_guid")
    protected String pspGuid;

    @Expose
    @SerializedName("encrypted_message")
    protected String encryptedMessage;

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

    public static OneyEncryptedRequest fromOneyPaymentRequest(OneyPaymentRequest request) throws DecryptException {
        OneyEncryptedRequest encryptedRequest = new OneyEncryptedRequest();
        encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), request.getEncryptKey());
        encryptedRequest.pspGuid = request.pspGuid;
        encryptedRequest.merchantGuid = request.merchantGuid;

        return encryptedRequest;
    }

    public static OneyEncryptedRequest fromOneyConfirmRequest(OneyConfirmRequest request) throws DecryptException {
        OneyEncryptedRequest encryptedRequest = new OneyEncryptedRequest();
        encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), request.getEncryptKey());

        return encryptedRequest;
    }

    public static OneyEncryptedRequest fromOneyRefundRequest(OneyRefundRequest request) throws DecryptException {
        OneyEncryptedRequest encryptedRequest = new OneyEncryptedRequest();
        encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(request.toString(), request.getEncryptKey());
        encryptedRequest.pspGuid = request.pspGuid;
        encryptedRequest.merchantGuid = request.merchantGuid;

        return encryptedRequest;
    }

    public static OneyEncryptedRequest fromJson(String message,
                                                ContractParametersCheckRequest contractParametersCheckRequest)
            throws DecryptException {
        String key = contractParametersCheckRequest.getPartnerConfiguration().getProperty(PARTNER_CHIFFREMENT_KEY);
        OneyEncryptedRequest encryptedRequest = new OneyEncryptedRequest();
        encryptedRequest.encryptedMessage = OneyRequest.encryptMessage(message, key);
        encryptedRequest.pspGuid = contractParametersCheckRequest.getPartnerConfiguration().getProperty(PSP_GUID_KEY);
        encryptedRequest.merchantGuid =
                contractParametersCheckRequest.getContractConfiguration().getProperty(MERCHANT_GUID_KEY).getValue();
        return encryptedRequest;
    }
}
