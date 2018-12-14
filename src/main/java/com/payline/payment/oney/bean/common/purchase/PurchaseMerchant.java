package com.payline.payment.oney.bean.common.purchase;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import static com.payline.payment.oney.utils.OneyConstants.MERCHANT_GUID_KEY;

public class PurchaseMerchant extends OneyBean {

    @SerializedName("merchant_guid")
    String merchantGuid;
    @SerializedName("external_reference")
    private String externalReference;
    @SerializedName("company_name")
    private String companyName;
    private String municipality;


    public String getMerchantGuid() {
        return merchantGuid;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getMunicipality() {
        return municipality;
    }

    private PurchaseMerchant() {
    }

    public PurchaseMerchant(PurchaseMerchant.Builder builder) {
        this.merchantGuid = builder.merchantGuid;
        this.externalReference = builder.externalReference;
        this.companyName = builder.companyName;
        this.municipality = builder.municipality;
    }

    public static class Builder {
        private String merchantGuid;
        private String externalReference;
        private String companyName;
        private String municipality;

        public static PurchaseMerchant.Builder aPurchaseMerchantBuilder() {
            return new PurchaseMerchant.Builder();
        }

        public PurchaseMerchant.Builder withMerchantGuid(String guid) {
            this.merchantGuid = guid;
            return this;
        }

        public PurchaseMerchant.Builder withExternalReference(String ref) {
            this.externalReference = ref;
            return this;
        }

        public PurchaseMerchant.Builder withCompanyName(String name) {
            this.companyName = name;
            return this;
        }

        public PurchaseMerchant.Builder withMunicipality(String city) {
            this.municipality = city;
            return this;
        }


        private PurchaseMerchant.Builder checkIntegrity() {
            if (this.merchantGuid == null) {
                throw new IllegalStateException("PurchaseMerchant must have a merchantGuid when built");
            }
            if (this.externalReference == null) {
                throw new IllegalStateException("PurchaseMerchant must have a externalReference when built");
            }
            return this;
        }
        
        public PurchaseMerchant.Builder fromPayline(PaymentRequest paymentRequest) {
            //Note HME mapping companyName (lot 2?)
            this.merchantGuid = paymentRequest.getContractConfiguration().getProperty(MERCHANT_GUID_KEY).getValue();
            this.externalReference = paymentRequest.getOrder().getReference();
//            this.companyName = null;
//            this.municipality = paymentRequest.getBuyer().getAddressForType(Buyer.AddressType.BILLING).getCity();

            return this;
        }

        public PurchaseMerchant build() {
            return new PurchaseMerchant(this.checkIntegrity());
        }
    }
}