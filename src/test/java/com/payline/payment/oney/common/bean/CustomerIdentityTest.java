package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.customer.CustomerIdentity;
import com.payline.pmapi.bean.common.Buyer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createDefaultBuyer;

public class CustomerIdentityTest {

    private CustomerIdentity customerIdentity;


    @Test
    public void customerIdentityTest() {
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
        Assertions.assertNotNull(customerIdentity);
    }


    @Test
    public void withoutBirthName() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                    .withPersonType(2)
                    .withHonorificCode(1)
                    .withFirstName("John")
                    .withLastName("LN")
                    .build();
        });
        Assertions.assertEquals("CustomerIdentity must have a birthName when built", exception.getMessage());

    }

    @Test
    public void withoutPersonType() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                    .withBirthName("Doe")
                    .withHonorificCode(1)
                    .withFirstName("John")
                    .build();
        });
        Assertions.assertEquals("CustomerIdentity must have a personType when built", exception.getMessage());

    }

    @Test
    public void withoutHonorificCode() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                    .withBirthName("Doe")
                    .withPersonType(2)
                    .withFirstName("John")
                    .build();
        });
        Assertions.assertEquals("CustomerIdentity must have a honorificCode when built", exception.getMessage());

    }

    @Test
    public void withoutFirstName() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                    .withBirthName("Doe")
                    .withPersonType(2)
                    .withHonorificCode(1)
                    .build();
        });
        Assertions.assertEquals("CustomerIdentity must have a firstName when built", exception.getMessage());

    }

    @Test
    public void wrongBirthDateFormat() {

        Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                    .withBirthName("Doe")
                    .withPersonType(2)
                    .withHonorificCode(1)
                    .withFirstName("John")
                    .withBithDate("1990-ZZ12-11")
                    .build();
        });
        Assertions.assertEquals("CustomerIdentity must have a birthDate in format 'yyyy-MM-dd' when built", exception.getMessage());

    }


    @Test
    public void fromPaylineBuyer() {
        Buyer buyer = createDefaultBuyer();
        customerIdentity = CustomerIdentity.Builder.aCustomerIdentity()
                .fromPayline(buyer)
                .build();
        Assertions.assertNotNull(customerIdentity.getFirstName());
        Assertions.assertNotNull(customerIdentity.getBirthDate());
        Assertions.assertNotNull(customerIdentity.getHonorificCode());
        Assertions.assertNotNull(customerIdentity.getPersonType());
        //Champs pas encore mappés
        Assertions.assertNull(customerIdentity.getGivenNames());
        Assertions.assertNull(customerIdentity.getBirthMunicipalityCode());
        Assertions.assertNull(customerIdentity.getCityzenshipCountryCode());

    }

    @Test
    public void testToString() {
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

        Assertions.assertTrue(customerIdentity.toString().contains("individual_taxpayer_code"));
        Assertions.assertTrue(customerIdentity.toString().contains("person_type"));
        Assertions.assertTrue(customerIdentity.toString().contains("honorific_code"));
        Assertions.assertTrue(customerIdentity.toString().contains("birth_name"));
        Assertions.assertTrue(customerIdentity.toString().contains("last_name"));
        Assertions.assertTrue(customerIdentity.toString().contains("first_name"));
        Assertions.assertTrue(customerIdentity.toString().contains("given_names"));
        Assertions.assertTrue(customerIdentity.toString().contains("birth_date"));
        Assertions.assertTrue(customerIdentity.toString().contains("birth_municipality_code"));
        Assertions.assertTrue(customerIdentity.toString().contains("birth_country_code"));
        Assertions.assertTrue(customerIdentity.toString().contains("citizenship_country_code"));
        Assertions.assertTrue(customerIdentity.toString().contains("company_name"));

    }

}
