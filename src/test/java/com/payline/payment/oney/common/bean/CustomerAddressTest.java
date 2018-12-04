package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.customer.CustomerAddress;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

public class CustomerAddressTest {

    private CustomerAddress customerAddress;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void customerAddressOK(){
        customerAddress = CustomerAddress.Builder.aCustomerAddressBuilder()
                .fromPayline(createDefaultBuyer())
                .build();

        Assert.assertNotNull(customerAddress);
        Assert.assertTrue(customerAddress.getLine1().length()<39);
        Assert.assertTrue(customerAddress.getLine2().length()<39);
        Assert.assertTrue(customerAddress.getLine3().length()<39);
        Assert.assertTrue(customerAddress.getLine4().length()<39);
        Assert.assertTrue(customerAddress.getLine5().length()<39);
    }

    @Test
    public void withoutLine1(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerAddress must have a line1 when built");

        customerAddress = CustomerAddress.Builder.aCustomerAddressBuilder()
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .withMunicipality("Mtp")
                .build();
    }

    @Test
    public void withoutPostalCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerAddress must have a postalCode when built");

        customerAddress = CustomerAddress.Builder.aCustomerAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withMunicipality("Mtp")
                .build();

    }

    @Test
    public void withoutCountryCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerAddress must have a countryCode when built");

        customerAddress = CustomerAddress.Builder.aCustomerAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .withMunicipality("Mtp")
                .build();

    }

    @Test
    public void withoutMunicipality(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("CustomerAddress must have a municipality when built");

        customerAddress = CustomerAddress.Builder.aCustomerAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .build();

    }
    @Test
    public void testToString(){

        customerAddress = CustomerAddress.Builder.aCustomerAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withLine2("residence ABC")
                .withLine3("bat D")
                .withLine4("etage E")
                .withLine5("porte F")
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .withMunicipality("mtp")
                .build();
        Assert.assertTrue(customerAddress.toString().contains("line1"));
        Assert.assertTrue(customerAddress.toString().contains("line2"));
        Assert.assertTrue(customerAddress.toString().contains("line3"));
        Assert.assertTrue(customerAddress.toString().contains("line4"));
        Assert.assertTrue(customerAddress.toString().contains("line5"));
        Assert.assertTrue(customerAddress.toString().contains("postal_code"));
        Assert.assertTrue(customerAddress.toString().contains("municipality"));
        Assert.assertTrue(customerAddress.toString().contains("country_code"));
        Assert.assertTrue(customerAddress.toString().contains("country_label"));


    }
}
