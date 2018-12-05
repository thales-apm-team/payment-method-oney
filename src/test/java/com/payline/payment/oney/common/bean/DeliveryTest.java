package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.OneyAddress;
import com.payline.payment.oney.bean.common.purchase.Delivery;
import com.payline.payment.oney.bean.common.purchase.Recipient;
import com.payline.pmapi.bean.common.Buyer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

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
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
                .build();

        Assert.assertNotNull(delivery.getAddressType());
        Assert.assertNotNull(delivery.getDeliveryDate());
        Assert.assertNotNull(delivery.getDeliveryOption());
        Assert.assertNotNull(delivery.getDeliveryModeCode());
        Assert.assertNotNull(delivery.getDeliveryAddress());
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
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
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
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
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
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
                .build();

    }

    @Test
    public void withoutAddressType() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a addressType when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
                .build();

    }

    @Test
    public void withoutRecipient() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a recipient when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withAddressType(5)
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
                .build();
    }

    @Test
    public void withoutDeliveryAddress() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Delivery must have a deliveryAddress when built");
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withAddressType(1)
                .withRecipient(null)

                .build();
    }


    @Test
    public void testToString() {
        delivery = Delivery.Builder.aDeliveryBuilder()
                .withDeliveryDate("1998-07-12")
                .withDeliveryModeCode(1)
                .withDeliveryOption(1)
                .withAddressType(1)
                .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                        .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                        .build())
                .build();
        Assert.assertTrue(delivery.toString().contains("delivery_date"));
        Assert.assertTrue(delivery.toString().contains("delivery_mode_code"));
        Assert.assertTrue(delivery.toString().contains("delivery_option"));
        Assert.assertTrue(delivery.toString().contains("address_type"));
        Assert.assertTrue(delivery.toString().contains("delivery_address"));

    }
}
