package com.payline.payment.oney.service.impl.request;

import com.payline.payment.oney.InvalidRequestException;
import com.payline.payment.oney.bean.common.*;
import com.payline.payment.oney.bean.common.payment.LoyaltyInformation;
import com.payline.payment.oney.bean.common.payment.NavigationData;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

public class OneyPaymentRequest extends OneyRequest {

    //todo definir les objets qui composent une requete de payment
    //v1 pour le moment


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

        public Builder withLoyaltyInformation(LoyaltyInformation loyaltyInformation) {
            this.loyaltyInformation = loyaltyInformation;
            return this;
        }

        public Builder withNavigation(NavigationData navigationData) {
            this.navigationData = navigationData;
            return this;
        }

        public Builder withmMrchantContext(String merchantContext) {
            this.merchantContext = merchantContext;
            return this;
        }

        public Builder withPspContext(String pspContext) {
            this.pspContext = pspContext;
            return this;
        }

        public Builder verifyIntegrity() {
            if (this.merchantGuid == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a merchantGuid when built");
            }
            if (this.pspGuid == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a pspGuid when built");
            }
            if (this.purchase == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a purchase when built");
            }
            if (this.customer == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a customer when built");
            }
            if (this.paymentData == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a paymentData when built");
            } else {
                return this;
            }
        }

        public OneyPaymentRequest build() {
            return new OneyPaymentRequest(this.verifyIntegrity());
        }

        public Builder fromPaylineRequest(PaymentRequest paylineRequest) throws InvalidRequestException {

            return OneyPaymentRequest.Builder.aOneyPaymentRequest()
                    .withLanguageCode("")
                    .withPspGuid("")
                    .withMerchantGuid("")
                    .withNavigation(NavigationData.Builder
                            .aNavigationDataBuilder()
                            .fromEnvironment(paylineRequest.getEnvironment())
                            .build())
                    .withPaymentdata(PaymentData.Builder
                            .aPaymentData()
                            .fromAmount(paylineRequest.getAmount())
                            .build())
                    .withPurchase(null)
                    .withCustomer(null)
                    ;

        }


    }

}
