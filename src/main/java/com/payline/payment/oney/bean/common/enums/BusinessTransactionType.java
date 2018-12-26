package com.payline.payment.oney.bean.common.enums;

public enum BusinessTransactionType {

    PNFCB("PNFCB"),
    AFFECTE("AFFECTE"),
    NONAMORTISSABLE("NONAMORTISSABLE");


    private String type;

    BusinessTransactionType(String value) {
        this.type = value;
    }

    public String getBusinessTrasactionType(){
        return type;
    }

    public static BusinessTransactionType fromOneyForm (String text) {

        for (BusinessTransactionType result : BusinessTransactionType.values()) {
            if (result.getBusinessTrasactionType().equals(text)) {
                return result;
            }
        }
        return null;
    }
}
