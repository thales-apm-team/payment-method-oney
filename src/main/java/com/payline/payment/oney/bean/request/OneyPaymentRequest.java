package com.payline.payment.oney.bean.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.LoyaltyInformation;
import com.payline.payment.oney.bean.common.NavigationData;
import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.payment.oney.bean.common.purchase.Purchase;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.utils.Required;

import java.util.Map;

public class OneyPaymentRequest extends OneyRequest {


    @SerializedName("language_code")
    private String languageCode;

    @SerializedName("skin_id")
    private int skinId; //(enum must be smarter (1 a 5)

    private String origin; //(WEB default value)

    @SerializedName("merchant_language_code")
    private String merchantLanguageCode; //(ISO 639-1)

    @Required
    @SerializedName("merchant_request_id")
    private String merchantRequestId;

    @Required
    private Purchase purchase;

    @Required
    private Customer customer;

    @Required
    @SerializedName("payment")
    private PaymentData paymentData;

    @SerializedName("loyalty_information")
    private LoyaltyInformation loyaltyInformation;

    @Required
    @SerializedName("navigation")
    private NavigationData navigationData;

    @SerializedName("merchant_context")
    private String merchantContext;

    @SerializedName("psp_context")
    private String pspContext;


    public String getLanguageCode() {
        return languageCode;
    }

    public int getSkinId() {
        return skinId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getMerchantLanguageCode() {
        return merchantLanguageCode;
    }

    public String getMerchantRequestId() {
        return merchantRequestId;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }

    public LoyaltyInformation getLoyaltyInformation() {
        return loyaltyInformation;
    }

    public NavigationData getNavigationData() {
        return navigationData;
    }

    public String getMerchantContext() {
        return merchantContext;
    }

    public String getPspContext() {
        return pspContext;
    }


    private OneyPaymentRequest(Builder builder) {
        this.merchantGuid = builder.merchantGuid;
        this.pspGuid = builder.pspGuid;
        this.languageCode = builder.languageCode;
        this.skinId = builder.skinId;
        this.origin = builder.origin;
        this.merchantLanguageCode = builder.merchantLanguageCode;
        this.merchantRequestId = builder.merchantRequestId;
        this.purchase = builder.purchase;
        this.customer = builder.customer;
        this.paymentData = builder.paymentData;
        this.loyaltyInformation = builder.loyaltyInformation;
        this.navigationData = builder.navigationData;
        this.merchantContext = builder.merchantContext;
        this.pspContext = builder.pspContext;
        this.encryptKey = builder.encryptKey;
        this.callParameters = builder.callParameters;
    }


    //Builder
    public static final class Builder {

        private String merchantGuid;
        private String pspGuid;
        private String languageCode;
        private int skinId; //(enum must be smarter (1 a 5)
        private String origin; //(WEB default value)
        private String merchantLanguageCode; //(ISO 639-1)
        private String merchantRequestId;
        private Purchase purchase;
        private Customer customer;
        private PaymentData paymentData;
        private LoyaltyInformation loyaltyInformation;
        private NavigationData navigationData;
        private String merchantContext;
        private String pspContext;
        private String encryptKey;
        private Map<String, String> callParameters;

        private Builder() {
        }


        public static Builder aOneyPaymentRequest() {
            return new Builder();
        }

        public Builder withMerchantGuid(String merchantGuid) {
            this.merchantGuid = merchantGuid;
            return this;
        }

        public Builder withPspGuid(String pspGuid) {
            this.pspGuid = pspGuid;
            return this;
        }

        public Builder withLanguageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder withSkinId(int skinId) {
            this.skinId = skinId;
            return this;
        }

        public Builder withOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder withMerchantLanguageCode(String merchantLanguageCode) {
            this.merchantLanguageCode = merchantLanguageCode;
            return this;
        }

        public Builder withMerchantRequestId(String merchantRequestId) {
            this.merchantRequestId = merchantRequestId;
            return this;
        }

        public Builder withPurchase(Purchase purchase) {
            this.purchase = purchase;
            return this;
        }


        public Builder withCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder withPaymentdata(PaymentData paymentData) {
            this.paymentData = paymentData;
            return this;
        }


        public Builder withNavigation(NavigationData navigationData) {
            this.navigationData = navigationData;
            return this;
        }


        public Builder withMerchantContext(String merchantContext) {
            this.merchantContext = merchantContext;
            return this;
        }

        public Builder withPspContext(String pspContext) {
            this.pspContext = pspContext;
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

        private Builder verifyIntegrity() throws InvalidDataException {

            if (this.merchantRequestId == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a merchantRequestId when built", "OneyPaymentRequest.merchantRequestId");
            }

            if (this.encryptKey == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a encryptKey when built", "OneyPaymentRequest.encryptKey");
            }

            if (this.callParameters == null || callParameters.isEmpty()) {
                throw new InvalidDataException("OneyPaymentRequest must have a callParameters when built", "OneyPaymentRequest.callParameters");
            }

            if (this.merchantGuid == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a merchantGuid when built", "OneyPaymentRequest.merchantGuid");
            }

            if (this.pspGuid == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a pspGuid when built", "OneyPaymentRequest.pspGuid");
            }

            if (this.purchase == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a purchase when built", "OneyPaymentRequest.purchase");
            }

            if (this.customer == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a customer when built", "OneyPaymentRequest.customer");
            }

            if (this.paymentData == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a paymentData when built", "OneyPaymentRequest.paymentData");
            }

            if (this.navigationData == null) {
                throw new InvalidDataException("OneyPaymentRequest must have a navigationData when built", "OneyPaymentRequest.navigationData");
            }


            return this;

        }

        public OneyPaymentRequest build() throws InvalidDataException {
            return new OneyPaymentRequest(this.verifyIntegrity());
        }

    }

}
