package com.payline.payment.oney.bean.common;

import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.pmapi.bean.common.Buyer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

public class OneyAddressTest {

    private OneyAddress oneyAddress;

    @Test
    public void customerAddressOK() throws Exception {
        oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                .fromPayline(createDefaultBuyer(), Buyer.AddressType.BILLING)
                .build();

        Assertions.assertNotNull(oneyAddress);
        Assertions.assertTrue(oneyAddress.getLine1().length() < 39);
        Assertions.assertTrue(oneyAddress.getLine2() == null || oneyAddress.getLine2().length() < 39);
        Assertions.assertTrue(oneyAddress.getLine3() == null || oneyAddress.getLine3().length() < 39);
        Assertions.assertTrue(oneyAddress.getLine4() == null || oneyAddress.getLine4().length() < 39);
        Assertions.assertTrue(oneyAddress.getLine5() == null || oneyAddress.getLine5().length() < 39);
    }

    @Test
    public void withoutLine1() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                    .withCountryCode("FRA")
                    .withCountryLabel("France")
                    .withPostalCode("34000")
                    .withMunicipality("Mtp")
                    .build();
        });
        Assertions.assertEquals("OneyAddress must have a line1 when built", exception.getMessage());

    }

    @Test
    public void withoutPostalCode() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                    .withLine1("12 place de la Comedie")
                    .withCountryCode("FRA")
                    .withCountryLabel("France")
                    .withMunicipality("Mtp")
                    .build();
        });
        Assertions.assertEquals("OneyAddress must have a postalCode when built", exception.getMessage());
    }

    @Test
    public void withoutCountryCode() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                    .withLine1("12 place de la Comedie")
                    .withCountryLabel("France")
                    .withPostalCode("34000")
                    .withMunicipality("Mtp")
                    .build();
        });
        Assertions.assertEquals("OneyAddress must have a countryCode when built", exception.getMessage());
    }

    @Test
    public void withoutMunicipality() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            oneyAddress = OneyAddress.Builder.aOneyAddressBuilder()
                    .withLine1("12 place de la Comedie")
                    .withCountryCode("FRA")
                    .withCountryLabel("France")
                    .withPostalCode("34000")
                    .build();
        });
        Assertions.assertEquals("OneyAddress must have a municipality when built", exception.getMessage());
    }

    @Test
    public void testToString() throws Exception {

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
        Assertions.assertTrue(oneyAddress.toString().contains("line1"));
        Assertions.assertTrue(oneyAddress.toString().contains("line2"));
        Assertions.assertTrue(oneyAddress.toString().contains("line3"));
        Assertions.assertTrue(oneyAddress.toString().contains("line4"));
        Assertions.assertTrue(oneyAddress.toString().contains("line5"));
        Assertions.assertTrue(oneyAddress.toString().contains("postal_code"));
        Assertions.assertTrue(oneyAddress.toString().contains("municipality"));
        Assertions.assertTrue(oneyAddress.toString().contains("country_code"));
        Assertions.assertTrue(oneyAddress.toString().contains("country_label"));


    }
}
