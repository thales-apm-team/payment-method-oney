package com.payline.payment.oney.utils;

import com.payline.payment.oney.bean.common.Customer;
import com.payline.payment.oney.bean.common.CustomerIdentity;
import com.payline.payment.oney.bean.common.payment.BusinessTransactionData;

public class BeanUtils {

    public static BusinessTransactionData createDefaultBusinessTransactionData(String code){

        return  BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withCode(code)
                .withVersion(1)
                .withBusinessTransactionType("type")
                .build();
    }

    public static Customer createDefaultCustomer(){

        return  Customer.Builder.aCustomBuilder()
                .withLanguageCode("FR")
                .withCustumerExternalCode("extCode")
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .build();
    }

    public static CustomerIdentity createDefaultCustomerIdentity(){

        return  CustomerIdentity.Builder.aCustomerIdentity()
                .withBirthName("Doe")
                .withPersonType(2)
                .withHonorificCode(1)
                .withFirstName("John")
                .build();
    }
}
