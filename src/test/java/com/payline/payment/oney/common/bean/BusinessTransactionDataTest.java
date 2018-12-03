package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.payment.BusinessTransactionData;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BusinessTransactionDataTest {

    private BusinessTransactionData businessTransactionData;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void businessTransactionDataOK() {
        businessTransactionData = BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withCode("24")
                .withBusinessTransactionType("type")
                .withVersion(1)
                .build();
        Assert.assertEquals("24", businessTransactionData.getCode());
        Assert.assertEquals("type", businessTransactionData.getBusinessTransactionType());
        Assert.assertEquals(1, businessTransactionData.getVersion(), 0);
    }


    @Test
    public void withoutCode() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("BusinessTransactionData must have a code when built");
        businessTransactionData = BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withBusinessTransactionType("type")
                .withVersion(1)
                .build();
    }

    @Test
    public void testToString() {
        businessTransactionData = BusinessTransactionData.Builder.aBusinessTransactionDataBuilder()
                .withCode("24")
                .withBusinessTransactionType("type")
                .withVersion(1)
                .build();
        Assert.assertTrue(businessTransactionData.toString().contains("code"));
        Assert.assertTrue(businessTransactionData.toString().contains("version"));
        Assert.assertTrue(businessTransactionData.toString().contains("business_transaction_type"));
    }

}


