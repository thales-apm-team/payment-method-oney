package com.payline.payment.oney.bean.common.customer;

import com.google.gson.annotations.SerializedName;

public class SupportingDocuments {

    @SerializedName("supporting_document_number")
    private String documentNumber;

    @SerializedName("family_code")
    private String familyCode;

    @SerializedName("type_code")
    private String typeCode;

    @SerializedName("issuing_authority")
    private String issuingAuthority;

    @SerializedName("issuing_date")
    private String issuingDate;

    @SerializedName("identity")
    private String expiringDate;


}
