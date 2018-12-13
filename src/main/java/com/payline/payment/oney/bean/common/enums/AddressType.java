package com.payline.payment.oney.bean.common.enums;

import com.payline.pmapi.bean.common.Buyer;

public enum AddressType {

    MERCHANT(1),
    THIRD_PART_RELAY_POINT(2),
    TRAVEL_AGENCY(3),
    BILLING(4),
    DELIVERY(5),
    ELECTRONIC_WAY(6);


    private int value;

    AddressType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static AddressType fromOneyForm (int value) {

        for (AddressType result : AddressType.values()) {
            if (result.getValue() == value) {
                return result;
            }
        }
        return null;
    }

    public static AddressType fromPaylineAddressType(Buyer.AddressType addressType){
        AddressType addressTypeOney;
        switch (addressType){
            case BILLING:
                addressTypeOney = AddressType.BILLING;
                break;
            case DELIVERY:
                addressTypeOney = AddressType.DELIVERY;
                break;

                default:
                    addressTypeOney= AddressType.BILLING;
        }


        return addressTypeOney;
    }

}
