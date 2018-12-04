package com.payline.payment.oney.bean.common.enums;

public enum CustomerType {

    COMPANY(1),
    INDIVIDUALS(2);


    private int value;

    CustomerType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static CustomerType fromOneyForm (int value) {

        for (CustomerType result : CustomerType.values()) {
            if (result.getValue() == value) {
                return result;
            }
        }
        return null;
    }
}
