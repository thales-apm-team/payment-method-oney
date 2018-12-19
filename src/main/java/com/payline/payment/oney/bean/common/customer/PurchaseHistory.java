package com.payline.payment.oney.bean.common.customer;

import com.google.gson.annotations.SerializedName;

/**
 * Pour v2
 */
@SuppressWarnings(value = "emptyClass")
public class PurchaseHistory {
    @SerializedName("total_number_of_purchase")
    private Integer numberOfPurchase;
    @SerializedName("total_amount")
    private float totalAmount;
    @SerializedName("first_purchase_date")
    private String firstPurchasedate;
    @SerializedName("last_purchase_date")
    private String lastPurchaseDate;

    public Integer getNumberOfPurchase() {
        return numberOfPurchase;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public String getFirstPurchasedate() {
        return firstPurchasedate;
    }

    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    //TODO Builder + mapping dans v2
}
