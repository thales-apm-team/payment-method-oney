package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.pmapi.bean.common.Amount;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.util.Currency;

public class PaymentDataTest {

    private PaymentData paymentdata;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void paymentDataOK() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .withCurrency("EUR")
                .withAmount(100)
                .build();

        Assert.assertEquals(100, paymentdata.getAmount().floatValue(),0.001);
        Assert.assertEquals("EUR", paymentdata.getCurrency());
    }

    @Test
    public void paymentDataFromAmount() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .fromAmount(new Amount(BigInteger.ONE, Currency.getInstance("EUR")))
                .build();

        Assert.assertEquals(1, paymentdata.getAmount().floatValue(),0.001);
        Assert.assertEquals("EUR", paymentdata.getCurrency());
    }

    @Test
    public void paymentDataFailAmount() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("PaymentData must have a amount when built");
        paymentdata = PaymentData.Builder.aPaymentData()
                .withCurrency("EUR")
                .build();

    }

    @Test
    public void paymentDataFailCurrency() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("PaymentData must have a currency when built");
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .build();

    }

}
