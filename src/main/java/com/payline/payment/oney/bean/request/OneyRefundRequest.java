package com.payline.payment.oney.bean.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.PurchaseCancel;
import com.payline.pmapi.bean.refund.request.RefundRequest;

import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.PluginUtils.generateMerchantRequestId;

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
    private PurchaseCancel purchase;
    //cle de chiffrement
    private transient String encryptKey;


    public PurchaseCancel getPurchase() {
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

    public OneyRefundRequest(OneyRefundRequest.Builder builder) {
        this.purchaseReference = builder.purchaseReference;
        this.languageCode = builder.languageCode;
        this.merchantRequestId = builder.merchantRequestId;
        this.purchase = builder.purchase;
        this.encryptKey = builder.encryptKey;
        this.merchantGuid = builder.merchantGuid;
        this.pspGuid = builder.pspGuid;
    }

    public static class Builder {
        private String purchaseReference;
        private String languageCode;
        private String merchantRequestId;
        private PurchaseCancel purchase;
        private String merchantGuid;
        private String pspGuid;
        private String encryptKey;

        public static OneyRefundRequest.Builder aOneyRefundRequest() {
            return new OneyRefundRequest.Builder();
        }

        public Builder withPurchase(PurchaseCancel purchase) {
            this.purchase = purchase;
            return this;
        }

        public Builder withPurchaseReference(String purchaseReference) {
            this.purchaseReference = purchaseReference;
            return this;
        }

        public Builder withLanguageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder withMerchantGuid(String merchantGuid) {
            this.merchantGuid = merchantGuid;
            return this;
        }

        public Builder withMerchantRequestId(String merchantGuid) {
            this.merchantRequestId = merchantGuid;
            return this;
        }

        public Builder withPspGuid(String pspGuid) {
            this.pspGuid = pspGuid;
            return this;
        }

        public Builder withEncryptKey(String key) {
            this.encryptKey = key;
            return this;
        }

        public OneyRefundRequest.Builder fromRefundRequest(RefundRequest refundRequest) {

            String merchantGuidValue = refundRequest.getContractConfiguration().getProperty(MERCHANT_GUID_KEY).getValue();

            this.purchaseReference = refundRequest.getTransactionId();
            this.merchantRequestId = generateMerchantRequestId(merchantGuidValue);
//            this.languageCode = refundRequest.getLocale() ;

            this.pspGuid = refundRequest.getPartnerConfiguration().getProperty(PSP_GUID_KEY);
            this.merchantGuid = merchantGuidValue;
            this.purchase = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                    .withAmount(refundRequest.getAmount().getAmountInSmallestUnit().floatValue())
                    // todo Mapper avec quoi ??
                    .withReasonCode(0)
                    // todo Mapper avec quoi ??
                    .withRefundFlag(true)
                    .build();
            this.encryptKey = refundRequest.getPartnerConfiguration().getProperty(CHIFFREMENT_KEY);

            return this;
        }

        private OneyRefundRequest.Builder verifyIntegrity() {
            if (this.merchantGuid == null) {
                throw new IllegalStateException("OneyRefundRequest must have a merchantGuid when built");
            }
            if (this.merchantRequestId == null) {
                throw new IllegalStateException("OneyRefundRequest must have a merchantRequestId when built");
            }
            if (this.pspGuid == null) {
                throw new IllegalStateException("OneyRefundRequest must have a pspGuid when built");
            }
            if (this.purchaseReference == null) {
                throw new IllegalStateException("OneyRefundRequest must have a reference when built");
            }
            if (this.purchase == null) {
                throw new IllegalStateException("OneyRefundRequest must have a purchase when built");
            }
            if (this.encryptKey == null) {
                throw new IllegalStateException("OneyRefundRequest must have a encryptKey when built");
            } else {
                return this;
            }

        }

        public OneyRefundRequest build() {
            return new OneyRefundRequest(this.verifyIntegrity());
        }

    }

}
