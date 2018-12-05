package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.OneyAddress;
import com.payline.pmapi.bean.common.Buyer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

public class OneyAddressTest {

    private OneyAddress oneyAddress;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void customerAddressOK(){
        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                .fromPayline(createDefaultBuyer(), Buyer.AddressType.BILLING)
                .build();

        Assert.assertNotNull(oneyAddress);
        Assert.assertTrue(oneyAddress.getLine1().length()<39);
        Assert.assertTrue(oneyAddress.getLine2() ==null || oneyAddress.getLine2().length()<39);
        Assert.assertTrue(oneyAddress.getLine3() ==null || oneyAddress.getLine3().length()<39);
        Assert.assertTrue(oneyAddress.getLine4() ==null || oneyAddress.getLine4().length()<39);
        Assert.assertTrue(oneyAddress.getLine5() ==null || oneyAddress.getLine5().length()<39);
    }

    @Test
    public void withoutLine1(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("OneyAddress must have a line1 when built");

        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .withMunicipality("Mtp")
                .build();
    }

    @Test
    public void withoutPostalCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("OneyAddress must have a postalCode when built");

        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withMunicipality("Mtp")
                .build();

    }

    @Test
    public void withoutCountryCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("OneyAddress must have a countryCode when built");

        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .withMunicipality("Mtp")
                .build();

    }

    @Test
    public void withoutMunicipality(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("OneyAddress must have a municipality when built");

        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                .withLine1("12 place de la Comedie")
                .withCountryCode("FRA")
                .withCountryLabel("France")
                .withPostalCode("34000")
                .build();

    }
    @Test
    public void testToString(){

        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
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
        Assert.assertTrue(oneyAddress.toString().contains("line1"));
        Assert.assertTrue(oneyAddress.toString().contains("line2"));
        Assert.assertTrue(oneyAddress.toString().contains("line3"));
        Assert.assertTrue(oneyAddress.toString().contains("line4"));
        Assert.assertTrue(oneyAddress.toString().contains("line5"));
        Assert.assertTrue(oneyAddress.toString().contains("postal_code"));
        Assert.assertTrue(oneyAddress.toString().contains("municipality"));
        Assert.assertTrue(oneyAddress.toString().contains("country_code"));
        Assert.assertTrue(oneyAddress.toString().contains("country_label"));


    }
}
