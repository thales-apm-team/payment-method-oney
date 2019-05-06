package com.payline.payment.oney.bean.response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.PurchaseNotification;
import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.exception.MalformedResponseException;
import com.payline.payment.oney.utils.properties.service.ConfigPropertiesEnum;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import static com.payline.payment.oney.utils.OneyConstants.CHIFFREMENT_IS_ACTIVE;

public class OneyNotificationResponse extends OneyResponse {
    private static final Logger LOGGER = LogManager.getLogger(OneyNotificationResponse.class);

    @SerializedName("language_code")
    private String languageCode;

    @SerializedName("merchant_guid")
    private String merchantGuid;

    @SerializedName("oney_request_id")
    private String oneyRequestId;

    @SerializedName("purchase")
    private PurchaseNotification purchase;

    @SerializedName("customer")
    private Customer customer;

    @SerializedName("psp_context")
    private String pspContext;

    public String getLanguageCode() {
        return languageCode;
    }

    public String getMerchantGuid() {
        return merchantGuid;
    }

    public String getOneyRequestId() {
        return oneyRequestId;
    }

    public PurchaseNotification getPurchase() {
        return purchase;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getPspContext() {
        return pspContext;
    }


    public static OneyNotificationResponse createTransactionStatusResponseFromJson(String json, String encryptKey)
            throws DecryptException, MalformedResponseException {
        Gson parser = new Gson();

        OneyNotificationResponse oneyNotificationResponse;
        try {
            oneyNotificationResponse = parser.fromJson(json, OneyNotificationResponse.class);
        }
        catch( JsonSyntaxException e){
            LOGGER.error("Unable to parse JSON content", e);
            throw new MalformedResponseException( e );
        }

        //Cas reponse est chiffree : on dechiffre la reponse afin de recuperer le statut de la transaction
        if (Boolean.valueOf(ConfigPropertiesEnum.INSTANCE.get(CHIFFREMENT_IS_ACTIVE))) {
            String decryptedMessage = OneyResponse.decryptMessage(oneyNotificationResponse.getEncryptedMessage(), encryptKey);

            return parser.fromJson(decryptedMessage, OneyNotificationResponse.class);

        }

        //Sinon on renvoie la reponse parsee
        return oneyNotificationResponse;
    }

}
