package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.customer.Customer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.BeanUtils.*;
import static com.payline.payment.oney.utils.TestUtils.createDefaultPaymentRequest;

public class CustomerTest {

    private Customer customer;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void customerOK(){
        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withLanguageCode("FR")
                .withCustumerExternalCode("code")
                .withTrustFlag(1)
                .withContactDetails(createDefaultContactDetails())
                .withCustomerAddress(createDefaultCustomerAdress())
                .build();

        Assert.assertNotNull(customer);
    }
    @Test
    public void withoutCustomerIdentity(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Customer must have a identity when built");

        customer = Customer.Builder.aCustomBuilder()
                .withLanguageCode("FR")
                .withCustumerExternalCode("code")
                .withTrustFlag(1)
                .withContactDetails(createDefaultContactDetails())
                .withCustomerAddress(createDefaultCustomerAdress())
                .build();
    }

    @Test
    public void withoutLanguageCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Customer must have a languageCode when built");

        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withCustumerExternalCode("code")
                .withContactDetails(createDefaultContactDetails())
                .withCustomerAddress(createDefaultCustomerAdress())
                .withTrustFlag(1)
                .build();

    }
    @Test
    public void withoutCustomerExternalCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Customer must have a customerExternalCode when built");

        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withLanguageCode("FR")
                .withContactDetails(createDefaultContactDetails())
                .withCustomerAddress(createDefaultCustomerAdress())
                .withTrustFlag(1)
                .build();

    }

    @Test
    public void withoutContactDetails(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Customer must have a contactDetails when built");

        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withLanguageCode("FR")
                .withCustumerExternalCode("code")
                .withCustomerAddress(createDefaultCustomerAdress())
                .withTrustFlag(1)
                .build();

    }
    @Test
    public void withoutCustomerAddress(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Customer must have a customerAddress when built");

        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withLanguageCode("FR")
                .withCustumerExternalCode("code")
                .withContactDetails(createDefaultContactDetails())
                .withTrustFlag(1)
                .build();

    }

    @Test
    public void fromPaylineRequest(){
       customer = Customer.Builder.aCustomBuilder()
           .fromPaylineRequest(createDefaultPaymentRequest())
           .build();
       Assert.assertNotNull(customer);
    }

    @Test
    public void testToString(){
        customer = createDefaultCustomer();

        Assert.assertTrue(customer.toString().contains("customer_external_code"));
        Assert.assertTrue(customer.toString().contains("language_code"));
        Assert.assertTrue(customer.toString().contains("identity"));
        Assert.assertTrue(customer.toString().contains("contact_details"));
        Assert.assertTrue(customer.toString().contains("customer_address"));

    }



}
