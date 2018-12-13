package com.payline.payment.oney.bean.common;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class AdditionalData extends OneyBean {


    @SerializedName("language_code")
    private String languageCode;
    @SerializedName("purchase")
    private AdditionalDataPurchase purchase;

    public String getLanguageCode() {
        return languageCode;
    }

    public AdditionalDataPurchase getPurchase() {
        return purchase;
    }


    public String toJson() {
        return new Gson().toJson(this);
    }

    public static AdditionalData fromJson(String jsonContent) {
        Gson gson = new Gson();
        return gson.fromJson(jsonContent, AdditionalData.class);
    }


    public class AdditionalDataPurchase {

        @SerializedName("status_code")
        private String statusCode;
        @SerializedName("status_label")
        private String statusLabel;
        @SerializedName("reason_code")
        private String reasonCode;
        @SerializedName("reason_label")
        private String reasonLabel;

        public String getStatusCode() {
            return statusCode;
        }

        public String getStatusLabel() {
            return statusLabel;
        }

        public String getReasonCode() {
            return reasonCode;
        }

        public String getReasonLabel() {
            return reasonLabel;
        }
    }
}
