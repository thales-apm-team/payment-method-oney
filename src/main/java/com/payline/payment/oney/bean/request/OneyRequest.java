package com.payline.payment.oney.bean.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.payment.oney.utils.Required;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class OneyRequest extends OneyBean {
    //Construit une requête Oney

    private static final Logger LOGGER = LogManager.getLogger(OneyRequest.class);

    @Required
    @SerializedName("merchant_guid")
    protected String merchantGuid;

    @Required
    @SerializedName("psp_guid")
    protected String pspGuid;

    @SerializedName("encrypted_message")
    protected String encryptedMessage;

    protected transient Map<String, String> callParameters;

    //cle de chiffrement
    protected transient String encryptKey;

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

    public String getEncryptKey() {
        return encryptKey;
    }

    public Map<String, String> getCallParameters() {
        return callParameters;
    }


}
