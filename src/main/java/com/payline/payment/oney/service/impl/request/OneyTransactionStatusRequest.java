package com.payline.payment.oney.service.impl.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;

public class OneyTransactionStatusRequest extends OneyRequest {

    @SerializedName("reference")
    private String purchaseReference;
    @SerializedName("language_code")
    private String languageCode;
    private transient  String encryptKey;


    public String getPurchaseReference() {
        return purchaseReference;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    private OneyTransactionStatusRequest(OneyTransactionStatusRequest.Builder builder) {
        this.purchaseReference = builder.purchaseReference;
        this.languageCode = builder.languageCode;
        this.pspGuid = builder.pspGuid;
        this.merchantGuid = builder.merchantGuid;
        this.encryptKey = builder.encryptKey;


    }

    public static class Builder {
        private String purchaseReference;
        private String languageCode;
        private String merchantGuid;
        private String pspGuid;
        private String encryptKey;

        public static OneyTransactionStatusRequest.Builder aOneyGetStatusRequest() {
            return new OneyTransactionStatusRequest.Builder();
        }


        //utile ??
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

        public Builder withPspGuid(String pspGuid) {
            this.pspGuid = pspGuid;
            return this;
        }
        public Builder withEncryptKey(String key) {
            this.encryptKey = key;
            return this;
        }

        public OneyTransactionStatusRequest.Builder fromTransactionStatusRequest(TransactionStatusRequest transactionStatusRequest) {
            return OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                    .withLanguageCode(transactionStatusRequest.getContractConfiguration().getProperty(OneyConstants.LANGUAGE_CODE_KEY).getValue())
                    .withMerchantGuid(transactionStatusRequest.getContractConfiguration().getProperty(OneyConstants.MERCHANT_GUID_KEY).getValue())
                    .withPspGuid(transactionStatusRequest.getContractConfiguration().getProperty(OneyConstants.PSP_GUID_KEY).getValue())
                    .withPurchaseReference(transactionStatusRequest.getTransactionId())
                    .withEncryptKey(transactionStatusRequest.getPartnerConfiguration().getProperty(OneyConstants.CHIFFREMENT_KEY));


        }

        private OneyTransactionStatusRequest.Builder verifyIntegrity() {
            if (this.merchantGuid == null) {
                throw new IllegalStateException("OneyConfirmRequest must have a merchantGuid when built");
            }

            if (this.pspGuid == null) {
                throw new IllegalStateException("OneyConfirmRequest must have a pspGuid when built");
            }
            if (this.purchaseReference == null) {
                throw new IllegalStateException("OneyConfirmRequest must have a reference when built");
            }
            if (this.encryptKey == null) {
                throw new IllegalStateException("OneyConfirmRequest must have a encryptKey when built");
            }else {
                return this;
            }

        }

        public OneyTransactionStatusRequest build() {
            return new OneyTransactionStatusRequest(this.verifyIntegrity());
        }

    }


}
