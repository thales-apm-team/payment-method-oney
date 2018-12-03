package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.payment.oney.bean.common.payment.PaymentType;
import com.payline.pmapi.bean.common.Amount;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.util.Currency;

import static com.payline.payment.oney.utils.http.BeanUtils.createDefaultBusinessTransactionData;

public class PaymentDataTest {

    private PaymentData paymentdata;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void paymentDataOK() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .withCurrency("EUR")
                .withAmount(100)
                .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
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
                .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                .build();

    }

    @Test
    public void paymentDataFailCurrency() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("PaymentData must have a currency when built");
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                .build();
    }


    @Test
    public void paymentDataFailBusinessTransactionData() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("PaymentData must have a businessTransaction when built");
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .withCurrency("EUR")
                .build();
    }

    @Test
    public void paymentDataOKWithoutBusinessTransactionData() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .withPaymentType(PaymentType.CHECK_CARD)
                .withCurrency("EUR")
                .build();
        Assert.assertNotNull(paymentdata);
    }

    @Test
    public void toStringTest(){
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .withCurrency("EUR")
                .withPaymentType(PaymentType.DEFERRED)
                .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                .build();

        System.out.println(paymentdata);
        Assert.assertTrue(paymentdata.toString().contains("payment_amount"));
        Assert.assertTrue(paymentdata.toString().contains("currency_code"));
        Assert.assertTrue(paymentdata.toString().contains("payment_type"));
        Assert.assertTrue(paymentdata.toString().contains("business_transaction"));


    }

}
