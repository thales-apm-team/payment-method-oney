package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.customer.ContactDetails;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

public class ContactDetailsTest {

    private ContactDetails contactDetails;

    @Rule
   public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void contactDetails() {
        contactDetails = ContactDetails.Builder.aContactDetailsBuilder()
                .withLandLineNumber("0436656565")
                .withMobilePhoneNumber("0636656565")
                .withEmailAdress("foo@bar.fr")
                .build();
        Assert.assertNotNull(contactDetails.getLandLineNumber());
        Assert.assertNotNull(contactDetails.getMobilePhoneNumber());
    }

    @Test
    public void withoutLandlineNumber() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("ContactDetails must have a landLineNumber when built");
        contactDetails = ContactDetails.Builder.aContactDetailsBuilder()
                .withMobilePhoneNumber("0636656565")
                .withEmailAdress("foo@bar.fr")
                .build();
    }

    @Test
    public void withoutMobilePhoneNumber() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("ContactDetails must have a mobilePhoneNumber when built");
        contactDetails = ContactDetails.Builder.aContactDetailsBuilder()
                .withLandLineNumber("0436656565")
                .withEmailAdress("foo@bar.fr")
                .build();
    }
    @Test
    public void withoutEmail() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("ContactDetails must have a  valid emailAddress when built");
        contactDetails = ContactDetails.Builder.aContactDetailsBuilder()
                .withLandLineNumber("0436656565")
                .withMobilePhoneNumber("0636656565")
                .build();
    }


    @Test
    public void fromPayline(){

        contactDetails = ContactDetails.Builder.aContactDetailsBuilder()
                .fromPayline(createDefaultBuyer())
                .build();
        Assert.assertNotNull(contactDetails.getLandLineNumber());
        Assert.assertNotNull(contactDetails.getMobilePhoneNumber());
        Assert.assertNotNull(contactDetails.getEmailAdress());
    }

    @Test
    public void testToString(){
        contactDetails = ContactDetails.Builder.aContactDetailsBuilder()
                .withLandLineNumber("0436656565")
                .withMobilePhoneNumber("0636656565")
                .withEmailAdress("foo@bar.fr")
                .build();
        Assert.assertTrue(contactDetails.toString().contains("landline_number"));
        Assert.assertTrue(contactDetails.toString().contains("mobile_phone_number"));
        Assert.assertTrue(contactDetails.toString().contains("email_address"));


    }


}
