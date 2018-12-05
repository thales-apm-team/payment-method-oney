package com.payline.payment.oney.bean.common.customer;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.common.Buyer;

import java.util.Map;

import static com.payline.payment.oney.utils.PluginUtils.truncateLongText;

public class CustomerAddress extends OneyBean {

    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String line5;
    @SerializedName("postal_code")
    private String postalCode;
    private String municipality;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("country_label")
    private String countryLabel;
    @SerializedName("arrondissement_code")
    private Integer arrondissementCode;


    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getLine4() {
        return line4;
    }

    public String getLine5() {
        return line5;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryLabel() {
        return countryLabel;
    }

    public Integer getArrondissementCode() {
        return arrondissementCode;
    }

    private CustomerAddress() {
    }

    private CustomerAddress(CustomerAddress.Builder builder) {
        this.line1 = builder.line1;
        this.line2 = builder.line2;
        this.line3 = builder.line3;
        this.line4 = builder.line4;
        this.line5 = builder.line5;
        this.postalCode = builder.postalCode;
        this.municipality = builder.municipality;
        this.countryCode = builder.countryCode;
        this.countryLabel = builder.countryLabel;
        this.arrondissementCode = builder.arrondissementCode;
    }

    public static class Builder {
        private String line1;
        private String line2;
        private String line3;
        private String line4;
        private String line5;
        private String postalCode;
        private String municipality;
        private String countryCode;
        private String countryLabel;
        private Integer arrondissementCode;


        public static CustomerAddress.Builder aCustomerAddressBuilder() {
            return new CustomerAddress.Builder();
        }

        public CustomerAddress.Builder withLine1(String line) {
            this.line1 = line;
            return this;
        }

        public CustomerAddress.Builder withLine2(String line) {
            this.line2 = line;
            return this;
        }

        public CustomerAddress.Builder withLine3(String line) {
            this.line3 = line;
            return this;
        }

        public CustomerAddress.Builder withLine4(String line) {
            this.line4 = line;
            return this;
        }

        public CustomerAddress.Builder withLine5(String line) {
            this.line5 = line;
            return this;
        }

        public CustomerAddress.Builder withPostalCode(String code) {
            this.postalCode = code;
            return this;
        }

        public CustomerAddress.Builder withMunicipality(String municipality) {
            this.municipality = municipality;
            return this;
        }

        public CustomerAddress.Builder withCountryCode(String code) {
            this.countryCode = code;
            return this;
        }

        public CustomerAddress.Builder withCountryLabel(String label) {
            this.countryLabel = label;
            return this;
        }

        public CustomerAddress.Builder withArrondissmentCode(Integer code) {
            this.arrondissementCode = code;
            return this;
        }

        private CustomerAddress.Builder checkIntegrity() {
            if (this.line1 == null) {
                throw new IllegalStateException("CustomerAddress must have a line1 when built");
            }
            if (this.countryLabel == null) {
                throw new IllegalStateException("CustomerAddress must have a countryLabel when built");
            }
            if (this.postalCode == null) {
                throw new IllegalStateException("CustomerAddress must have a postalCode when built");
            }
            if (this.municipality == null) {
                throw new IllegalStateException("CustomerAddress must have a municipality when built");
            }
            if (this.countryCode == null) {
                throw new IllegalStateException("CustomerAddress must have a countryCode when built");
            } else return this;

        }

        public CustomerAddress.Builder fromPayline(Buyer buyer) {

            String street = buyer.getAddressForType(Buyer.AddressType.BILLING).getStreet1();
            String street2 = buyer.getAddressForType(Buyer.AddressType.BILLING).getStreet2();
            this.truncateAddress(street, street2);

            this.municipality = buyer.getAddressForType(Buyer.AddressType.BILLING).getCity();
            this.postalCode = buyer.getAddressForType(Buyer.AddressType.BILLING).getZipCode();
            this.countryLabel = buyer.getAddressForType(Buyer.AddressType.BILLING).getCountry();
            //todo CountryCODE ISO alpha-3 a partir du country label
            this.countryCode = buyer.getAddressForType(Buyer.AddressType.BILLING).getCountry();
//            this.arrondissementCode = buyer.getAddressForType(Buyer.AddressType.BILLING);


            return this;
        }

// Découpe l'adresse intelligemment
        private void truncateAddress(String street1, String street2) {
          Map addressTruncated =  truncateLongText(street1, street2, 38);

            if(addressTruncated.get("line1")!=null) {
                this.line1 = addressTruncated.get("line1").toString();
            }
            if(addressTruncated.get("line2")!=null) {
                this.line2 = addressTruncated.get("line2").toString();
            }
            if(addressTruncated.get("line3")!=null) {
                this.line3 = addressTruncated.get("line3").toString();
            }
            if(addressTruncated.get("line4")!=null) {
                this.line4 = addressTruncated.get("line4").toString();
            }
            if(addressTruncated.get("line5")!=null) {
                this.line5 = addressTruncated.get("line5").toString();
            }
        }

        public CustomerAddress build() {
            return new CustomerAddress(this.checkIntegrity());
        }
    }

}
