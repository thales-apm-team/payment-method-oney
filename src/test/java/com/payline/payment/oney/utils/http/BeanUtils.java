package com.payline.payment.oney.utils.http;

import com.payline.payment.oney.bean.common.payment.BusinessTransactionData;

import java.util.Random;

public class BeanUtils {

    public static BusinessTransactionData createDefaultBusinessTransactionData(String code){

        return  BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withCode(code)
                .withVersion(1)
                .withBusinessTransactionType("type")
                .build();
    }



}
