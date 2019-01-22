package com.payline.payment.oney.bean.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.service.impl.RequestConfigServiceImpl;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.refund.request.RefundRequest;

import java.util.Map;

public class OneyTransactionStatusRequest extends OneyRequest {

    @SerializedName("reference")
    private String purchaseReference;

    @SerializedName("language_code")
    private String languageCode;


    public String getPurchaseReference() {
        return purchaseReference;
    }

    public String getLanguageCode() {
        return languageCode;
    }


    private OneyTransactionStatusRequest(OneyTransactionStatusRequest.Builder builder) {
        this.purchaseReference = builder.purchaseReference;
        this.languageCode = builder.languageCode;
        this.pspGuid = builder.pspGuid;
        this.merchantGuid = builder.merchantGuid;
        this.encryptKey = builder.encryptKey;
        this.callParameters = builder.callParameters;

    }

    public static class Builder {
        private String purchaseReference;
        private String languageCode;
        private String merchantGuid;
        private String pspGuid;
        private String encryptKey;
        private Map<String, String> callParameters;

        public static OneyTransactionStatusRequest.Builder aOneyGetStatusRequest() {
            return new OneyTransactionStatusRequest.Builder();
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

        public Builder withPspGuid(String pspGuid) {
            this.pspGuid = pspGuid;
            return this;
        }

        public Builder withEncryptKey(String key) {
            this.encryptKey = key;
            return this;
        }

        public Builder withCallParameters(Map<String, String> parameters) {
            this.callParameters = parameters;
            return this;
        }

        public OneyTransactionStatusRequest.Builder fromTransactionStatusRequest(TransactionStatusRequest transactionStatusRequest) throws InvalidDataException {
            return OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                    .withLanguageCode(RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, OneyConstants.LANGUAGE_CODE_KEY))
                    .withMerchantGuid(RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, OneyConstants.MERCHANT_GUID_KEY))
                    .withPspGuid(RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, OneyConstants.PSP_GUID_KEY))
                    .withPurchaseReference(transactionStatusRequest.getTransactionId())
                    .withEncryptKey(RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, OneyConstants.PARTNER_CHIFFREMENT_KEY))
                    .withCallParameters(PluginUtils.getParametersMap(transactionStatusRequest));


        }

        //Creer une transactionStatusRequest depuis une refund Request
        public OneyTransactionStatusRequest.Builder fromRefundRequest(RefundRequest refundRequest) throws InvalidDataException {
            return OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                    .withLanguageCode(RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, OneyConstants.LANGUAGE_CODE_KEY))
                    .withMerchantGuid(RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, OneyConstants.MERCHANT_GUID_KEY))
                    .withPspGuid(RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, OneyConstants.PSP_GUID_KEY))
                    .withPurchaseReference(refundRequest.getTransactionId())
                    .withEncryptKey(RequestConfigServiceImpl.INSTANCE.getParameterValue(refundRequest, OneyConstants.PARTNER_CHIFFREMENT_KEY))
                    .withCallParameters(PluginUtils.getParametersMap(refundRequest));


        }

        private OneyTransactionStatusRequest.Builder verifyIntegrity() throws InvalidDataException {
            if (this.merchantGuid == null) {
                throw new InvalidDataException("OneyTransactionStatusRequest must have a merchantGuid when built", "OneyTransactionStatusRequest.merchantGuid");
            }

            if (this.pspGuid == null) {
                throw new InvalidDataException("OneyTransactionStatusRequest must have a pspGuid when built", "OneyTransactionStatusRequest.pspGuid");
            }

            if (this.purchaseReference == null) {
                throw new InvalidDataException("OneyTransactionStatusRequest must have a reference when built", "OneyTransactionStatusRequest.reference");
            }

            if (this.encryptKey == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a encryptKey when built", "OneyTransactionStatusRequest.encryptKey");
            }

            if (this.callParameters == null || callParameters.isEmpty()) {
                throw new InvalidDataException("OneyTransactionStatusRequest must have a callParameters when built", "OneyTransactionStatusRequest.callParameters");
            }

            return this;

        }

        public OneyTransactionStatusRequest build() throws InvalidDataException {
            return new OneyTransactionStatusRequest(this.verifyIntegrity());
        }

    }


}
