package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.payment.BusinessTransactionData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BusinessTransactionDataTest {

    private BusinessTransactionData businessTransactionData;


    @Test
    public void businessTransactionDataOK() {
        businessTransactionData = BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withCode("24")
                .withBusinessTransactionType("type")
                .withVersion(1)
                .build();
        Assertions.assertEquals("24", businessTransactionData.getCode());
        Assertions.assertEquals("type", businessTransactionData.getBusinessTransactionType());
        Assertions.assertEquals(Integer.valueOf(1), businessTransactionData.getVersion());
    }


    @Test
    public void withoutCode() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            businessTransactionData = BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                    .withBusinessTransactionType("type")
                    .withVersion(1)
                    .build();
        });
        Assertions.assertEquals("BusinessTransactionData must have a code when built", exception.getMessage());
    }

    @Test
    public void testToString() {
        businessTransactionData = BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withCode("24")
                .withBusinessTransactionType("type")
                .withVersion(1)
                .build();
        Assertions.assertTrue(businessTransactionData.toString().contains("code"));
        Assertions.assertTrue(businessTransactionData.toString().contains("version"));
        Assertions.assertTrue(businessTransactionData.toString().contains("business_transaction_type"));
    }

}


