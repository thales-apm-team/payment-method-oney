package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.OneyAddress;
import com.payline.payment.oney.bean.common.purchase.Delivery;
import com.payline.pmapi.bean.common.Buyer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

public class DeliveryTest {


    private Delivery delivery;


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

        Assertions.assertNotNull(delivery.getAddressType());
        Assertions.assertNotNull(delivery.getDeliveryDate());
        Assertions.assertNotNull(delivery.getDeliveryOption());
        Assertions.assertNotNull(delivery.getDeliveryModeCode());
        Assertions.assertNotNull(delivery.getDeliveryAddress());
    }

    @Test
    public void wrongDate() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            delivery = Delivery.Builder.aDeliveryBuilder()
                    .withDeliveryDate("111998-07-12")
                    .withDeliveryModeCode(1)
                    .withDeliveryOption(1)
                    .withAddressType(1)
                    .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                            .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                            .build())
                    .build();
        });
        Assertions.assertEquals("Delivery must have a deliveryDate in format 'yyyy-MM-dd' when built", exception.getMessage());


    }

    @Test
    public void withoutDeliveryModeCode() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            delivery = Delivery.Builder.aDeliveryBuilder()
                    .withDeliveryDate("1998-07-12")
                    .withDeliveryOption(1)
                    .withAddressType(1)
                    .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                            .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                            .build())
                    .build();
        });
        Assertions.assertEquals("Delivery must have a deliveryModeCode when built", exception.getMessage());


    }

    @Test
    public void withoutDeliveryOption() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            delivery = Delivery.Builder.aDeliveryBuilder()
                    .withDeliveryDate("1998-07-12")
                    .withDeliveryModeCode(1)
                    .withAddressType(1)
                    .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                            .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                            .build())
                    .build();
        });
        Assertions.assertEquals("Delivery must have a deliveryOption when built", exception.getMessage());


    }

    @Test
    public void withoutAddressType() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            delivery = Delivery.Builder.aDeliveryBuilder()
                    .withDeliveryDate("1998-07-12")
                    .withDeliveryModeCode(1)
                    .withDeliveryOption(1)
                    .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                            .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                            .build())
                    .build();
        });
        Assertions.assertEquals("Delivery must have a addressType when built", exception.getMessage());


    }

    @Test
    public void withoutRecipient() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            delivery = Delivery.Builder.aDeliveryBuilder()
                    .withDeliveryDate("1998-07-12")
                    .withDeliveryModeCode(1)
                    .withDeliveryOption(1)
                    .withAddressType(5)
                    .withDeliveryAddress(OneyAddress.Builder.aOneyAddressBuilder()
                            .fromPayline(createDefaultBuyer(), Buyer.AddressType.DELIVERY)
                            .build())
                    .build();
        });
        Assertions.assertEquals("Delivery must have a recipient when built", exception.getMessage());

    }

    @Test
    public void withoutDeliveryAddress() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            delivery = Delivery.Builder.aDeliveryBuilder()
                    .withDeliveryDate("1998-07-12")
                    .withDeliveryModeCode(1)
                    .withDeliveryOption(1)
                    .withAddressType(1)
                    .withRecipient(null)
                    .build();
        });
        Assertions.assertEquals("Delivery must have a deliveryAddress when built", exception.getMessage());

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
        Assertions.assertTrue(delivery.toString().contains("delivery_date"));
        Assertions.assertTrue(delivery.toString().contains("delivery_mode_code"));
        Assertions.assertTrue(delivery.toString().contains("delivery_option"));
        Assertions.assertTrue(delivery.toString().contains("address_type"));
        Assertions.assertTrue(delivery.toString().contains("delivery_address"));

    }
}
