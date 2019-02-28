package com.payline.payment.oney.bean.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.service.impl.RequestConfigServiceImpl;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.Required;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;

import java.util.Map;

import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.PluginUtils.createFloatAmount;
import static com.payline.payment.oney.utils.PluginUtils.generateMerchantRequestId;

public class OneyConfirmRequest extends OneyRequest {

    @SerializedName("reference")
    private String purchaseReference;

    //RequestBody
    @Expose
    @SerializedName("language_code")
    private String languageCode;

    @Required
    @Expose
    @SerializedName("merchant_request_id")
    private String merchantRequestId;

    @Required
    @Expose
    @SerializedName("payment")
    private PaymentData paymentData;

    public String getPurchaseReference() {
        return purchaseReference;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getMerchantRequestId() {
        return merchantRequestId;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }


    private OneyConfirmRequest(Builder builder) {
        this.purchaseReference = builder.purchaseReference;
        this.languageCode = builder.languageCode;
        this.merchantRequestId = builder.merchantRequestId;
        this.paymentData = builder.paymentData;
        this.pspGuid = builder.pspGuid;
        this.merchantGuid = builder.merchantGuid;
        this.encryptKey = builder.encryptKey;
        this.callParameters = builder.callParameters;


    }

    public static class Builder {
        private String purchaseReference;
        private String languageCode;
        private String merchantRequestId;
        private PaymentData paymentData;
        private String merchantGuid;
        private String pspGuid;
        private String encryptKey;
        private Map<String, String> callParameters;

        public Builder(RedirectionPaymentRequest paymentRequest) throws InvalidDataException {
            String merchantGuidValue = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentRequest, MERCHANT_GUID_KEY);
            this.purchaseReference = paymentRequest.getRequestContext().getRequestData().get(EXTERNAL_REFERENCE_KEY);
            this.languageCode = paymentRequest.getRequestContext().getRequestData().get(LANGUAGE_CODE_KEY);
            this.merchantRequestId = generateMerchantRequestId(merchantGuidValue);

            this.pspGuid = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentRequest, PSP_GUID_KEY);
            this.merchantGuid = merchantGuidValue;
            this.paymentData = PaymentData.Builder.aPaymentData()
                    .withAmount(createFloatAmount(paymentRequest.getAmount().getAmountInSmallestUnit(), paymentRequest.getAmount().getCurrency()))
                    .buildForConfirmRequest();
            this.encryptKey = RequestConfigServiceImpl.INSTANCE.getParameterValue(paymentRequest, PARTNER_CHIFFREMENT_KEY);
            this.callParameters = PluginUtils.getParametersMap(paymentRequest);
        }


        public Builder(TransactionStatusRequest transactionStatusRequest) throws InvalidDataException {
            String merchantGuidValue = RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, MERCHANT_GUID_KEY);

            this.purchaseReference = transactionStatusRequest.getTransactionId();
            this.merchantRequestId = generateMerchantRequestId(merchantGuidValue);

            this.pspGuid = RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, PSP_GUID_KEY);
            this.merchantGuid = merchantGuidValue;
            this.paymentData = PaymentData.Builder.aPaymentData()
                    .withAmount(createFloatAmount(transactionStatusRequest.getAmount().getAmountInSmallestUnit(), transactionStatusRequest.getAmount().getCurrency()))
                    .buildForConfirmRequest();
            this.encryptKey = RequestConfigServiceImpl.INSTANCE.getParameterValue(transactionStatusRequest, PARTNER_CHIFFREMENT_KEY);
            this.callParameters = PluginUtils.getParametersMap(transactionStatusRequest);
        }

        public OneyConfirmRequest build() throws InvalidDataException {
            this.verifyIntegrity();
            return new OneyConfirmRequest(this);
        }


        private void verifyIntegrity() throws InvalidDataException {

            if (this.merchantGuid == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a merchantGuid when built", "OneyConfirmRequest.merchantGuid");
            }

            if (this.merchantRequestId == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a merchantRequestId when built", "OneyConfirmRequest.merchantRequestId");
            }

            if (this.pspGuid == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a pspGuid when built", "OneyConfirmRequest.pspGuid");
            }

            if (this.purchaseReference == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a reference when built", "OneyConfirmRequest.reference");
            }

            if (this.paymentData == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a paymentData when built", "OneyConfirmRequest.paymentData");
            }

            if (this.encryptKey == null) {
                throw new InvalidDataException("OneyConfirmRequest must have a encryptKey when built", "OneyConfirmRequest.encryptKey");
            }

            if (this.callParameters == null || callParameters.isEmpty()) {
                throw new InvalidDataException("OneyConfirmRequest must have a callParameters when built", "OneyConfirmRequest.callParameters");
            }

        }

    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

}
