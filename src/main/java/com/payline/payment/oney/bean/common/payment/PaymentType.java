package com.payline.payment.oney.bean.common.payment;

/**
 * Contains all available values for a paymentType
 */
public enum PaymentType {


    IMMEDIATE(0),
    DEFERRED(1),
    CHECK_CARD(2);

    private int value;

    PaymentType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
    public static PaymentType fromOneyForm (int value) {

        for (PaymentType result : PaymentType.values()) {
            if (result.getValue() == value) {
                return result;
            }
        }
        return null;
    }



}
