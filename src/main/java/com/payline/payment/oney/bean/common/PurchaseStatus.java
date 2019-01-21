package com.payline.payment.oney.bean.common;

import com.google.gson.annotations.SerializedName;

public class PurchaseStatus {

    @SerializedName("status_label")
    private String statusLabel;
    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("reason_code")
    private String reasonCode;
    @SerializedName("reason_label")
    private String reasonLabel;

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonLabel() {
        return reasonLabel;
    }


    public PurchaseStatus(String statusLabel, String statusCode, String reasonCode, String reasonLabel) {
        this.statusLabel = statusLabel;
        this.statusCode = statusCode;
        this.reasonCode = reasonCode;
        this.reasonLabel = reasonLabel;
    }


}
