package com.payline.payment.oney.utils;

import com.payline.payment.oney.bean.common.customer.ContactDetails;
import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.customer.CustomerAddress;
import com.payline.payment.oney.bean.common.customer.CustomerIdentity;
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
                .withContactDetails(createDefaultContactDetails())
                .withCustomerAddress(createDefaultCustomerAdress())
                .build();
    }

    public static ContactDetails createDefaultContactDetails(){
        return ContactDetails.Builder.aContactDetailsBuilder()
                .withLandLineNumber("0436656565")
                .withMobilePhoneNumber("0636656565")
                .withEmailAdress("foo@bar.fr")
                .build();
    }

    public static CustomerAddress createDefaultCustomerAdress(){
        return CustomerAddress.Builder.aCustomerAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withLine2("residence ABC")
                .withLine3("bat D")
                .withLine4("etage E")
                .withLine5("porte F")
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .withMunicipality("mtp")
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
