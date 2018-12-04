package com.payline.payment.oney.bean.common.purchase;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;

public class Purchase extends OneyBean {

    @SerializedName("external_reference_type")
    private String externalReferenceType; //CMDE
    @SerializedName("external_reference")
    private String externalReference;
    @SerializedName("purchase_amount")
    private Float purchaseAmount;
    @SerializedName("currency_code")
    private String currencyCode; //ISO 4217
    @SerializedName("purchase_merchant")
    private PurchaseMerchant purchaseMerchant; //CMDE
}
