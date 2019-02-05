package com.payline.payment.oney.bean.common;

import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.exception.InvalidDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.BeanUtils.*;
import static com.payline.payment.oney.utils.TestUtils.createDefaultPaymentRequest;

public class CustomerTest {

    private Customer customer;


    @Test
    public void customerOK() throws Exception {
        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withLanguageCode("FR")
                .withCustumerExternalCode("code")
                .withTrustFlag(1)
                .withContactDetails(createDefaultContactDetails())
                .withCustomerAddress(createDefaultCustomerAdress())
                .build();

        Assertions.assertNotNull(customer);
    }

    @Test
    public void withoutCustomerIdentity() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            customer = Customer.Builder.aCustomBuilder()
                    .withLanguageCode("FR")
                    .withCustumerExternalCode("code")
                    .withTrustFlag(1)
                    .withContactDetails(createDefaultContactDetails())
                    .withCustomerAddress(createDefaultCustomerAdress())
                    .build();
        });
        Assertions.assertEquals("Customer must have a identity when built", exception.getMessage());

    }

    @Test
    public void withoutLanguageCode() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            customer = Customer.Builder.aCustomBuilder()
                    .withCustomerIdentity(createDefaultCustomerIdentity())
                    .withCustumerExternalCode("code")
                    .withContactDetails(createDefaultContactDetails())
                    .withCustomerAddress(createDefaultCustomerAdress())
                    .withTrustFlag(1)
                    .build();
        });
        Assertions.assertEquals("Customer must have a languageCode when built", exception.getMessage());


    }

    @Test
    public void withoutCustomerExternalCode() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            customer = Customer.Builder.aCustomBuilder()
                    .withCustomerIdentity(createDefaultCustomerIdentity())
                    .withLanguageCode("FR")
                    .withContactDetails(createDefaultContactDetails())
                    .withCustomerAddress(createDefaultCustomerAdress())
                    .withTrustFlag(1)
                    .build();
        });
        Assertions.assertEquals("Customer must have a customerExternalCode when built", exception.getMessage());


    }

    @Test
    public void withoutContactDetails() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            customer = Customer.Builder.aCustomBuilder()
                    .withCustomerIdentity(createDefaultCustomerIdentity())
                    .withLanguageCode("FR")
                    .withCustumerExternalCode("code")
                    .withCustomerAddress(createDefaultCustomerAdress())
                    .withTrustFlag(1)
                    .build();
        });
        Assertions.assertEquals("Customer must have a contactDetails when built", exception.getMessage());


    }

    @Test
    public void withoutCustomerAddress() {

        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            customer = Customer.Builder.aCustomBuilder()
                    .withCustomerIdentity(createDefaultCustomerIdentity())
                    .withLanguageCode("FR")
                    .withCustumerExternalCode("code")
                    .withContactDetails(createDefaultContactDetails())
                    .withTrustFlag(1)
                    .build();
        });
        Assertions.assertEquals("Customer must have a customerAddress when built", exception.getMessage());


    }

    @Test
    public void fromPaylineRequest() throws Exception {
        customer = Customer.Builder.aCustomBuilder()
                .fromPaylineRequest(createDefaultPaymentRequest())
                .build();
        Assertions.assertNotNull(customer);
    }

    @Test
    public void testToString() throws Exception {
        customer = createDefaultCustomer();

        Assertions.assertTrue(customer.toString().contains("customer_external_code"));
        Assertions.assertTrue(customer.toString().contains("language_code"));
        Assertions.assertTrue(customer.toString().contains("identity"));
        Assertions.assertTrue(customer.toString().contains("contact_details"));
        Assertions.assertTrue(customer.toString().contains("customer_address"));

    }


}
