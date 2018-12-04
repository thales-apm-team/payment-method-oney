package com.payline.payment.oney.bean.common;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.pmapi.bean.common.Buyer;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomerIdentity extends OneyBean {

    @SerializedName("individual_taxpayer_code")
    private String taxpayerCode;
    @SerializedName("person_type")
    private Integer personType; //or enum ?? 1-2
    @SerializedName("honorific_code")
    private Integer honorificCode; //or enum ?? 1-2-3
    @SerializedName("birth_name")
    private String birthName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("given_names")
    private String givenNames;
    @SerializedName("birth_date")
    private String birthDate;
    @SerializedName("birth_municipality_code")
    private String birthMunicipalityCode; // INSEE code
    @SerializedName("birth_arrondissement_code")
    private Integer birthArrondissementCode;
    @SerializedName("birth_country_code")
    private String birthCountryCode;
    @SerializedName("citizenship_country_code")
    private String cityzenshipCountryCode; // INSEE code
    @SerializedName("company_name")
    private String companyName; // INSEE code //Mandatory if person type  = 1


    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public Integer getPersonType() {
        return personType;
    }

    public Integer getHonorificCode() {
        return honorificCode;
    }

    public String getBirthName() {
        return birthName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthMunicipalityCode() {
        return birthMunicipalityCode;
    }

    public Integer getBirthArrondissementCode() {
        return birthArrondissementCode;
    }

    public String getCityzenshipCountryCode() {
        return cityzenshipCountryCode;
    }

    public String getCompanyName() {
        return companyName;

    }

    public String getBirthCountryCode() {
        return birthCountryCode;
    }


    private CustomerIdentity() {
    }

    public CustomerIdentity(CustomerIdentity.Builder builder) {
        this.taxpayerCode = builder.taxpayerCode;
        this.personType = builder.personType;
        this.honorificCode = builder.honorificCode;
        this.birthName = builder.birthName;
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.givenNames = builder.givenNames;
        this.birthDate = builder.birthDate;
        this.birthMunicipalityCode = builder.birthMunicipalityCode;
        this.birthArrondissementCode = builder.birthArrondissementCode;
        this.birthCountryCode = builder.birthCountryCode;
        this.cityzenshipCountryCode = builder.cityzenshipCountryCode;
        this.companyName = builder.companyName;
    }

    public static class Builder {
        private String taxpayerCode;
        private Integer personType; //or enum ?? 1-2
        private Integer honorificCode; //or enum ?? 1-2-3
        private String birthName;
        private String lastName;
        private String firstName;
        private String givenNames;
        private String birthDate;
        private String birthMunicipalityCode; // INSEE code
        private Integer birthArrondissementCode;
        private String birthCountryCode; // INSEE code
        private String cityzenshipCountryCode; // INSEE code
        private String companyName; // INSEE code //Mandatory if person type  = 1

        public static CustomerIdentity.Builder aCustomerIdentity() {
            return new CustomerIdentity.Builder();
        }

        public CustomerIdentity.Builder withTaxpayerCode(String code) {
            this.taxpayerCode = code;
            return this;
        }

        public CustomerIdentity.Builder withPersonType(Integer code) {
            this.personType = code;
            return this;
        }

        public CustomerIdentity.Builder withHonorificCode(Integer code) {
            this.honorificCode = code;
            return this;
        }

        public CustomerIdentity.Builder withBirthName(String name) {
            this.birthName = name;
            return this;
        }

        public CustomerIdentity.Builder withLastName(String name) {
            this.lastName = name;
            return this;
        }

        public CustomerIdentity.Builder withFirstName(String name) {
            this.firstName = name;
            return this;
        }

        public CustomerIdentity.Builder withGivenNames(String name) {
            this.givenNames = name;
            return this;
        }

        public CustomerIdentity.Builder withBithDate(String date) {
            this.birthDate = date;
            return this;
        }

        public CustomerIdentity.Builder withBirthMunicipalityCode(String code) {
            this.birthMunicipalityCode = code;
            return this;
        }

        public CustomerIdentity.Builder withBirthCountryCode(String code) {
            this.birthCountryCode = code;
            return this;
        }

        public CustomerIdentity.Builder withCitizenCountryCode(String code) {
            this.cityzenshipCountryCode = code;
            return this;
        }

        public CustomerIdentity.Builder withBirthArrondissementCode(Integer code) {
            this.birthArrondissementCode = code;
            return this;
        }

        public CustomerIdentity.Builder withCompanyName(String name) {
            this.companyName = name;
            return this;
        }

        public CustomerIdentity.Builder fromPayline(Buyer buyer) {
            //todo
            this.taxpayerCode = null;
//            this.taxpayerCode = buyer.getLegalDocument();
            this.personType = PluginUtils.getPersonType(buyer.getLegalStatus());
            this.honorificCode = PluginUtils.getHonorificCode(buyer.getFullName().getCivility());
            this.birthName = buyer.getFullName().getLastName();

            if (this.honorificCode == 2 || this.honorificCode == 3) {
                this.lastName = buyer.getFullName().getLastName();
            }
            this.firstName = buyer.getFullName().getFirstName();
            if (buyer.getBirthday() != null) {
                this.birthDate = new SimpleDateFormat("yyyy-MM-dd").format(buyer.getBirthday());
            }
            //Champs a mapper dans les prochains lots
            this.givenNames = null;
            this.birthMunicipalityCode = null;
            this.birthArrondissementCode = null;
            this.birthCountryCode = null;
            this.cityzenshipCountryCode = null;
            this.companyName = null;

            return this;
        }

        private CustomerIdentity.Builder verifyIntegrity() {
            if (this.personType == null) {
                throw new IllegalStateException("CustomerIdentity must have a personType when built");
            }
            if (this.honorificCode == null) {
                throw new IllegalStateException("CustomerIdentity must have a honorificCode when built");
            }
            if (this.birthName == null) {
                throw new IllegalStateException("CustomerIdentity must have a birthName when built");
            }
            if (this.firstName == null) {
                throw new IllegalStateException("CustomerIdentity must have a firstName when built");
            }
            if (this.personType == 1 && this.companyName == null) {
                throw new IllegalStateException("CustomerIdentity must have a companyName when built");
            }
            if (this.birthDate != null && !this.birthDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalStateException("CustomerIdentity must have a birthDate in format 'yyyy-MM-dd' when built");
            }
            return this;

        }

        public CustomerIdentity build() {
            return new CustomerIdentity(this.verifyIntegrity());
        }
    }
}
