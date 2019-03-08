package com.payline.payment.oney.bean.common;

import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.InvalidFieldFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoyaltyInformationTest {

    private LoyaltyInformation loyaltyInformation;


    @Test
    public void loyaltyInformationOK() throws Exception {
        loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                .withLoyaltyId("1")
                .withType("mytype")
                .withLoyatyFaqUrl("some/url")
                .withExpirationDate("2018-04-24")
                .withValue("25")
                .build();
        Assertions.assertEquals("1", loyaltyInformation.getLoyaltyId());
        Assertions.assertEquals("mytype", loyaltyInformation.getType());
        Assertions.assertEquals("some/url", loyaltyInformation.getLoyaltyFaqUrl());
        Assertions.assertEquals("2018-04-24", loyaltyInformation.getExpirationDate());
        Assertions.assertEquals("25", loyaltyInformation.getValue());
    }

    @Test
    public void withoutId() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {

            loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                    .withType("mytype")
                    .withLoyatyFaqUrl("some/url")
                    .withExpirationDate("2018-04-24")
                    .withValue("25")
                    .build();
        });
        Assertions.assertEquals("LoyaltyInformation must have a loyaltyId when built", exception.getMessage());
    }

    @Test
    public void wrongDateFormat() {

        Throwable exception = Assertions.assertThrows(InvalidFieldFormatException.class, () -> {
            loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                    .withLoyaltyId("25")
                    .withExpirationDate("zZ2018-04-24")
                    .build();
        });
        Assertions.assertEquals("LoyaltyInformation must have a expirationDate in format 'yyyy-MM-dd' when built", exception.getMessage());
    }

    @Test
    public void testToString() throws Exception {
        loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                .withLoyaltyId("1")
                .withType("mytype")
                .withLoyatyFaqUrl("some/url")
                .withExpirationDate("2018-04-24")
                .withValue("25")
                .build();
        Assertions.assertTrue(loyaltyInformation.toString().contains("loyalty_id"));
        Assertions.assertTrue(loyaltyInformation.toString().contains("loyalty_faq_url"));
        Assertions.assertTrue(loyaltyInformation.toString().contains("expiration_date"));
        Assertions.assertTrue(loyaltyInformation.toString().contains("type"));
        Assertions.assertTrue(loyaltyInformation.toString().contains("value"));
    }
}
