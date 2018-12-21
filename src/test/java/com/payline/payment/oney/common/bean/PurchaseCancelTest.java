package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.PurchaseCancel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PurchaseCancelTest {

    private PurchaseCancel purchaseCancel;


    @Test
    public void testPurchaseCancel() {
        purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                .withRefundFlag(true)
                .withReasonCode(0)
                .withAmount(Float.valueOf("25"))
                .build();

        Assertions.assertNotNull(purchaseCancel);
    }


    @Test
    public void testPurchaseCancel1() {
        purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                .withRefundFlag(true)
                .withReasonCode(1)
                .build();

        Assertions.assertNotNull(purchaseCancel);
    }


    @Test
    public void withoutReasonCode() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {

            purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                    .withRefundFlag(true)
                    .withAmount(Float.valueOf("25"))
                    .build();
        });
        Assertions.assertEquals("PurchaseCancel must have a valid reasonCode when built", exception.getMessage());

    }

    @Test
    public void withInvalidReasonCode() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {

            purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                    .withRefundFlag(true)
                    .withReasonCode(4)
                    .withAmount(Float.valueOf("25"))
                    .build();
        });
        Assertions.assertEquals("PurchaseCancel must have a valid reasonCode when built", exception.getMessage());
    }

    @Test
    public void withoutRefundFlag() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {

            purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                    .withReasonCode(1)
                    .withAmount(Float.valueOf("25"))
                    .build();
        });
        Assertions.assertEquals("PurchaseCancel must have a refundFlag when built", exception.getMessage());
    }

    @Test
    public void withoutAmount() {
        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {

            purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                    .withRefundFlag(true)
                    .withReasonCode(0)
                    .build();
        });
        Assertions.assertEquals("PurchaseCancel must have a amount when built", exception.getMessage());
    }

    @Test
    public void testToString(){
        purchaseCancel = PurchaseCancel.Builder.aPurchaseCancelBuilder()
                .withRefundFlag(true)
                .withReasonCode(0)
                .withAmount(Float.valueOf("25"))
                .build();

        Assertions.assertTrue(purchaseCancel.toString().contains("refund_down_payment"));
        Assertions.assertTrue(purchaseCancel.toString().contains("cancellation_reason_code"));
        Assertions.assertTrue(purchaseCancel.toString().contains("cancellation_amount"));


    }
}
