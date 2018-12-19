package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.enums.PaymentType;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.BeanUtils.createDefaultBusinessTransactionData;
import static com.payline.payment.oney.utils.TestUtils.createCompletePaymentBuilder;

public class PaymentDataTest {

    private PaymentData paymentdata;

    @Test
    public void paymentDataOK() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .withCurrency("EUR")
                .withAmount(100)
                .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                .build();

        Assertions.assertEquals(100, paymentdata.getAmount(), 0.001);
        Assertions.assertEquals("EUR", paymentdata.getCurrency());
    }

    @Test
    public void paymentDataFromPayline() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .fromPayline(createCompletePaymentBuilder().build())
                .build();

        Assertions.assertEquals(10, paymentdata.getAmount(), 0.001);
        Assertions.assertEquals("EUR", paymentdata.getCurrency());
    }

    @Test
    public void paymentDataFailAmount() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            paymentdata = PaymentData.Builder.aPaymentData()
                    .withCurrency("EUR")
                    .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                    .build();
        });
        Assertions.assertEquals("PaymentData must have a amount when built", exception.getMessage());


    }

    @Test
    public void paymentDataFailCurrency() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            paymentdata = PaymentData.Builder.aPaymentData()
                    .withAmount(100)
                    .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                    .build();
        });
        Assertions.assertEquals("PaymentData must have a currency when built", exception.getMessage());

    }


    @Test
    public void paymentDataFailBusinessTransactionData() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            paymentdata = PaymentData.Builder.aPaymentData()
                    .withAmount(100)
                    .withCurrency("EUR")
                    .build();
        });
        Assertions.assertEquals("PaymentData must have a businessTransaction when built", exception.getMessage());

    }

    @Test
    public void paymentDataOKWithoutBusinessTransactionData() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .withPaymentType(PaymentType.CHECK_CARD.getValue())
                .withCurrency("EUR")
                .build();
        Assertions.assertNotNull(paymentdata);
    }

    @Test
    public void toStringTest() {
        paymentdata = PaymentData.Builder.aPaymentData()
                .withAmount(100)
                .withCurrency("EUR")
                .withPaymentType(PaymentType.DEFERRED.getValue())
                .withBusinessTransactionList(createDefaultBusinessTransactionData("254"))
                .build();

        Assertions.assertTrue(paymentdata.toString().contains("payment_amount"));
        Assertions.assertTrue(paymentdata.toString().contains("currency_code"));
        Assertions.assertTrue(paymentdata.toString().contains("payment_type"));
        Assertions.assertTrue(paymentdata.toString().contains("business_transaction"));


    }

}
