package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.purchase.Delivery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DeliveryTest {


    private Delivery delivery;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testDelivery() {
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withAddressType(1)
                .withRecipient(null)
                .build();

        Assert.assertNotNull(delivery.getAddressType());
        Assert.assertNotNull(delivery.getDeliveryDate());
        Assert.assertNotNull(delivery.getDeliveryOption());
        Assert.assertNotNull(delivery.getDeliveryModeCode());
    }

    @Test
    public void wrongDate() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a deliveryDate in format 'yyyy-MM-dd' when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("111998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withAddressType(1)
                .build();

    }

    @Test
    public void withoutDeliveryModeCode() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a deliveryModeCode when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryOption(1)
                .withAddressType(1)
                .build();

    }

    @Test
    public void withoutDeliveryOption() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a deliveryOption when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withAddressType(1)
                .build();

    }

    @Test
    public void withoutAddresstype() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a addressType when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .build();

    }

    @Test
    public void testwithoutRecipient() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a recipient when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withAddressType(5)
                .build();
    }
}
