package com.payline.payment.oney.bean.common;

import com.google.gson.annotations.SerializedName;

public class LoyaltyInformation extends OneyBean {

    @SerializedName("loyalty_id")
    private String loyaltyId;
    @SerializedName("loyalty_faq_url")
    private String loyaltyFaqUrl;
    @SerializedName("expiration_date")
    private String expirationDate; //date yyyy-MM-dd
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;

    // Getter
    public String getLoyaltyId() {
        return loyaltyId;
    }

    public String getLoyaltyFaqUrl() {
        return loyaltyFaqUrl;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public LoyaltyInformation(LoyaltyInformation.Builder builder) {
        this.loyaltyId = builder.loyaltyId;
        this.loyaltyFaqUrl = builder.loyaltyFaqUrl;
        this.expirationDate = builder.expirationDate;
        this.type = builder.type;
        this.value = builder.value;
    }

    //Constructor
    private LoyaltyInformation(){}

    // Builder

    public static class Builder {
        private String loyaltyId;
        private String loyaltyFaqUrl;
        private String expirationDate; //date yyyy-MM-dd
        private String type;
        private String value;


        public static LoyaltyInformation.Builder aLoyaltyInformationBuilder(){
            return new LoyaltyInformation.Builder();
        }

        public LoyaltyInformation.Builder withLoyaltyId(String id){
            this.loyaltyId = id;
            return this;
        }

        public LoyaltyInformation.Builder withLoyatyFaqUrl(String url){
            this.loyaltyFaqUrl = url;
            return this;
        }

        public LoyaltyInformation.Builder withExpirationDate(String date){
            this.expirationDate = date;
            return this;
        }

        public LoyaltyInformation.Builder withType(String type){
            this.type = type;
            return this;
        }

        public LoyaltyInformation.Builder withValue(String value){
            this.value = value;
            return this;
        }

        private LoyaltyInformation.Builder verifyIntegrity() {
            if (this.loyaltyId == null) {
                throw new IllegalStateException("LoyaltyInformation must have a loyaltyId when built");
            }
            if (this.expirationDate != null && !this.expirationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalStateException("LoyaltyInformation must have a expirationDate in format 'yyyy-MM-dd' when built");
            }
            else {
                return this;
            }
        }

        public LoyaltyInformation build(){
            return new LoyaltyInformation(this.verifyIntegrity());
        }
    }
}
