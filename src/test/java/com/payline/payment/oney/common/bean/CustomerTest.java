package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.Customer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.BeanUtils.createDefaultCustomerIdentity;

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
                .build();
    }

    @Test
    public void withoutLanguageCode(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Customer must have a languageCode when built");

        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withCustumerExternalCode("code")
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
                .withTrustFlag(1)
                .build();

    }

    @Test
    public void testToString(){
        customer = Customer.Builder.aCustomBuilder()
                .withCustomerIdentity(createDefaultCustomerIdentity())
                .withLanguageCode("FR")
                .withCustumerExternalCode("code")
                .withTrustFlag(1)
                .build();

        Assert.assertTrue(customer.toString().contains("trust_flag"));
        Assert.assertTrue(customer.toString().contains("customer_external_code"));
        Assert.assertTrue(customer.toString().contains("language_code"));
        Assert.assertTrue(customer.toString().contains("identity"));

    }

}
