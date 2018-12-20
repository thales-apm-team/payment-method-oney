package com.payline.payment.oney.service.impl.request;

import com.google.gson.annotations.SerializedName;

/**
 * Pour lot2
 */

public class OneyRefundRequest extends OneyRequest {

    @SerializedName("reference")
    private String purchaseReference;
    //RequestBody
    @SerializedName("language_code")
    private String languageCode;
    @SerializedName("merchant_request_id")
    private String merchantRequestId;
    @SerializedName("purchase")
    private CancelPurchase purchase;
    //cle de chiffrement
    private transient String encryptKey;


    public CancelPurchase getPurchase() {
        return purchase;
    }
    public String getPurchaseReference() {
        return purchaseReference;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getMerchantRequestId() {
        return merchantRequestId;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public class CancelPurchase {
        @SerializedName("cancellation_reason_code")
        private int reason; //or enum (0 = cancellation, 1 = fraud)
        @SerializedName("cancellation_amount")
        private float amount; //must be present  if cancellationReason == 1  !! default value == total amount of payment
        @SerializedName("refund_down_payment")
        private boolean refundFlag; // must be present  if cancellationReason == 1 true if is refunded else false

        //todo builder + method validate
    }

}
