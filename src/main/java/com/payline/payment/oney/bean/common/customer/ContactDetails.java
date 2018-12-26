package com.payline.payment.oney.bean.common.customer;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.common.Buyer;

public class ContactDetails extends OneyBean {

    @SerializedName("landline_number")
    private String landLineNumber;
    @SerializedName("mobile_phone_number")
    private String mobilePhoneNumber;
    @SerializedName("email_address")
    private String emailAdress;

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getEmailAdress() {
        return emailAdress;
    }
    private ContactDetails() {
    }

    private ContactDetails(ContactDetails.Builder builder) {
        this.landLineNumber = builder.landLineNumber;
        this.mobilePhoneNumber = builder.mobilePhoneNumber;
        this.emailAdress = builder.emailAdress;
    }

    public static class Builder {
        private String landLineNumber;
        private String mobilePhoneNumber;
        private String emailAdress;

        public static ContactDetails.Builder aContactDetailsBuilder() {
            return new ContactDetails.Builder();
        }

        public ContactDetails.Builder withLandLineNumber(String number) {
            this.landLineNumber = number;
            return this;
        }

        public ContactDetails.Builder withMobilePhoneNumber(String number) {
            this.mobilePhoneNumber = number;
            return this;
        }
        public ContactDetails.Builder withEmailAdress(String address) {
            this.emailAdress = address;
            return this;
        }
        private ContactDetails.Builder verifyIntegrity() {
            //.matches("\\d{10}") ?
            if (this.landLineNumber == null) {
                throw new IllegalStateException("ContactDetails must have a landLineNumber when built");
            }

            if (this.mobilePhoneNumber == null) {
                throw new IllegalStateException("ContactDetails must have a mobilePhoneNumber when built");
            }

            if (this.emailAdress == null) {
                throw new IllegalStateException("ContactDetails must have a  valid emailAddress when built");
            }
            return this;
        }

        public ContactDetails.Builder fromPayline(Buyer buyer) {
            this.mobilePhoneNumber = buyer.getPhoneNumberForType(Buyer.PhoneNumberType.CELLULAR);
            this.landLineNumber = buyer.getPhoneNumberForType(Buyer.PhoneNumberType.HOME);
            this.emailAdress = buyer.getEmail();
            return this;
        }

        public ContactDetails build() {
            return new ContactDetails(this.verifyIntegrity());
        }


    }
}
