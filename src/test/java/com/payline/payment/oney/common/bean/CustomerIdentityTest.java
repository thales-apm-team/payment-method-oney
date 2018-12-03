package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.CustomerIdentity;
import com.payline.payment.oney.bean.common.payment.LoyaltyInformation;
import com.sun.media.sound.InvalidFormatException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CustomerIdentityTest {

    private CustomerIdentity customerIdentity;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void customerIdentityTest(){
     customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
             .withBirthName("Doe")
             .withPersonType(2)
             .withHonorificCode(1)
             .withFirstName("John")
             //Optinals
             .withLastName("LN")
             .withGivenNames("GN")
             .withBithDate("1990-12-11")
             .build();
    }


    @Test
    public void withoutBirthName(){

        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerIdentity must have a birthName when built");

        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .withPersonType(2)
                .withHonorificCode(1)
                .withFirstName("John")
                .withLastName("LN")
             .build();
    }
    @Test
    public void withoutPersonType() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerIdentity must have a personType when built");

        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .withBirthName("Doe")
                .withHonorificCode(1)
                .withFirstName("John")
                .build();
    }
    @Test
    public void withoutHonorificCode() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerIdentity must have a honorificCode when built");

        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .withBirthName("Doe")
                .withPersonType(2)
                .withFirstName("John")
                .build();
    }
    @Test
    public void withoutFirstName() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerIdentity must have a firstName when built");

        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .withBirthName("Doe")
                .withPersonType(2)
                .withHonorificCode(1)
                .build();
    }

    @Test
    public void wrongBirthDateFormat() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerIdentity must have a birthDate in format 'yyyy-MM-dd' when built");

        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .withBirthName("Doe")
                .withPersonType(2)
                .withHonorificCode(1)
                .withFirstName("John")
                .withBithDate("1990-ZZ12-11")
                .build();
    }

    @Test
    public void testToString(){
        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .withBirthName("Doe")
                .withPersonType(2)
                .withHonorificCode(1)
                .withFirstName("John")
                //Optinals
                .withLastName("LN")
                .withGivenNames("GN")
                .withBithDate("1990-12-11")
                .withBirthCountryCode("FR")
                .withTaxpayerCode("34000")
                .withBirthMunicipalityCode("75018")
                .withBirthArrondissementCode(18)
                .withCitizenCountryCode("FRA")
                .withCompanyName("DKA")
                .build();

        System.out.println(customerIdentity);
        Assert.assertTrue(customerIdentity.toString().contains("individual_taxpayer_code"));
        Assert.assertTrue(customerIdentity.toString().contains("person_type"));
        Assert.assertTrue(customerIdentity.toString().contains("honorific_code"));
        Assert.assertTrue(customerIdentity.toString().contains("birth_name"));
        Assert.assertTrue(customerIdentity.toString().contains("last_name"));
        Assert.assertTrue(customerIdentity.toString().contains("first_name"));
        Assert.assertTrue(customerIdentity.toString().contains("given_names"));
        Assert.assertTrue(customerIdentity.toString().contains("birth_date"));
        Assert.assertTrue(customerIdentity.toString().contains("birth_municipality_code"));
        Assert.assertTrue(customerIdentity.toString().contains("birth_country_code"));
        Assert.assertTrue(customerIdentity.toString().contains("citizenship_country_code"));
        Assert.assertTrue(customerIdentity.toString().contains("company_name"));

    }

}
