package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.payment.LoyaltyInformation;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LoyaltyInformationTest {

    private LoyaltyInformation loyaltyInformation;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void loyaltyInformationOK() {
        loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                .withLoyaltyId("1")
                .withType("mytype")
                .withLoyatyFaqUrl("some/url")
                .withExpirationDate("2018-04-24")
                .withValue("25")
                .build();
        Assert.assertEquals("1",loyaltyInformation.getLoyaltyId());
        Assert.assertEquals("mytype",loyaltyInformation.getType());
        Assert.assertEquals("some/url",loyaltyInformation.getLoyaltyFaqUrl());
        Assert.assertEquals("2018-04-24",loyaltyInformation.getExpirationDate());
        Assert.assertEquals("25",loyaltyInformation.getValue());
    }

    @Test
    public void withoutId() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("LoyaltyInformation must have a loyaltyId when built");
        loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                .withType("mytype")
                .withLoyatyFaqUrl("some/url")
                .withExpirationDate("2018-04-24")
                .withValue("25")
                .build();
    }

    @Test
    public void wrongDateFormat() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("LoyaltyInformation must have a expirationDate in format 'yyyy-MM-dd' when built");
        loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                .withLoyaltyId("25")
                .withExpirationDate("zZ2018-04-24")
                .build();
    }

    @Test
    public void testToString(){
        loyaltyInformation = LoyaltyInformation.Builder.aLoyaltyInformationBuilder()
                .withLoyaltyId("1")
                .withType("mytype")
                .withLoyatyFaqUrl("some/url")
                .withExpirationDate("2018-04-24")
                .withValue("25")
                .build();
        Assert.assertTrue(loyaltyInformation.toString().contains("loyalty_id"));
        Assert.assertTrue(loyaltyInformation.toString().contains("loyalty_faq_url"));
        Assert.assertTrue(loyaltyInformation.toString().contains("expiration_date"));
        Assert.assertTrue(loyaltyInformation.toString().contains("type"));
        Assert.assertTrue(loyaltyInformation.toString().contains("value"));
    }
}
