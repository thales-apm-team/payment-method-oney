package com.payline.payment.oney.service.impl.request;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.LoyaltyInformation;
import com.payline.payment.oney.bean.common.NavigationData;
import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.payment.oney.bean.common.purchase.Purchase;
import com.payline.payment.oney.exception.InvalidRequestException;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.payline.payment.oney.utils.OneyConstants.CHIFFREMENT_KEY;
import static com.payline.payment.oney.utils.OneyConstants.MERCHANT_GUID_KEY;
import static com.payline.payment.oney.utils.OneyConstants.PSP_GUID_KEY;
import static com.payline.payment.oney.utils.PluginUtils.generateMerchantRequestId;

public class OneyPaymentRequest extends OneyRequest {


    @SerializedName("language_code")
    private String languageCode;
    @SerializedName("skin_id")
    private int skinId; //(enum must be smarter (1 a 5)
    private String origin; //(WEB default value)
    @SerializedName("merchant_language_code")
    private String merchantLanguageCode; //(ISO 639-1)
    @SerializedName("merchant_request_id")
    private String merchantRequestId;
    private Purchase purchase;
    private Customer customer;
    @SerializedName("payment")
    private PaymentData paymentData;
    @SerializedName("loyalty_information")
    private LoyaltyInformation loyaltyInformation;
    @SerializedName("navigation")
    private NavigationData navigationData;
    @SerializedName("merchant_context")
    private String merchantContext;
    @SerializedName("psp_context")
    private String pspContext;

    //cle de chiffrement
    private transient String encryptKey;

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

    public String getEncryptKey() {
        return encryptKey;
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

        private Builder verifyIntegrity() {
            if (this.merchantGuid == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a merchantGuid when built");
            }
            if (this.merchantRequestId == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a merchantRequestId when built");
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
            }
            if (this.encryptKey == null) {
                throw new IllegalStateException("OneyPaymentRequest must have a encryptKey when built");
            } else {
                return this;
            }
        }

        public OneyPaymentRequest build() {
            return new OneyPaymentRequest(this.verifyIntegrity());
        }

        public Builder fromPaylineRequest(PaymentRequest paymentRequest) throws InvalidRequestException {

            String merchGuid = paymentRequest.getContractConfiguration().getProperty(MERCHANT_GUID_KEY).getValue();

            return OneyPaymentRequest.Builder.aOneyPaymentRequest()
                    .withLanguageCode(paymentRequest.getLocale().getLanguage())
                    .withMerchantRequestId(generateMerchantRequestId(merchGuid))
                    .withPspGuid(paymentRequest.getPartnerConfiguration().getProperty(PSP_GUID_KEY))
                    .withMerchantGuid(merchGuid)
                    .withNavigation(NavigationData.Builder
                            .aNavigationDataBuilder()
                            .fromEnvironment(paymentRequest.getEnvironment())
                            .build())
                    .withPaymentdata(PaymentData.Builder
                            .aPaymentData()
                            .fromPayline(paymentRequest)
                            .build())
                    .withCustomer(Customer.Builder.aCustomBuilder()
                            .fromPaylineRequest(paymentRequest)
                            .build())
                    .withPurchase(Purchase.Builder.aPurchaseBuilder()
                            .fromPayline(paymentRequest)
                            .build())
                    .withMerchantLanguageCode(paymentRequest.getLocale().getLanguage())
                    .withEncryptKey(paymentRequest.getPartnerConfiguration().getProperty(CHIFFREMENT_KEY))
                    .withMerchantContext(paymentRequest.getSoftDescriptor())
                    .withPspContext(paymentRequest.getTransactionId())
//                    .withOrigin("WEB")
//                    .withSkinId(3)

                    ;

        }


    }

}
