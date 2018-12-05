package com.payline.payment.oney.bean.common.enums;

public enum HonorificCode {

    UNDEFINED(0),
    MR(1),
    MADAM(2),
    MISS(3);


    private int value;

    HonorificCode(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static HonorificCode fromOneyForm (int value) {

        for (HonorificCode result : HonorificCode.values()) {
            if (result.getValue() == value) {
                return result;
            }
        }
        return null;
    }

}
