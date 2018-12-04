package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.purchase.PurchaseMerchant;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.BeanUtils.*;

public class PurchaseMerchantTest {

    private PurchaseMerchant purchaseMerchant;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void purchaseMerchantOK(){
        purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                .withCompanyName("cie")
                .withExternalReference("ref")
                .withMerchantGuid("guid")
                .withMunicipality("city")
                .build();

        Assert.assertNotNull(purchaseMerchant);
        Assert.assertNotNull(purchaseMerchant.getExternalReference());
        Assert.assertNotNull(purchaseMerchant.getMerchantGuid());
        Assert.assertTrue(purchaseMerchant.toString().contains("company_name"));
        Assert.assertTrue(purchaseMerchant.toString().contains("external_reference"));
        Assert.assertTrue(purchaseMerchant.toString().contains("merchant_guid"));
        Assert.assertTrue(purchaseMerchant.toString().contains("municipality"));
    }


    @Test
    public void withoutExternalRef(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("PurchaseMerchant must have a externalReference when built");
        purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                .withCompanyName("cie")
                .withMerchantGuid("guid")
                .withMunicipality("city")
                .build();

        Assert.assertNotNull(purchaseMerchant);
        Assert.assertNotNull(purchaseMerchant.getExternalReference());
        Assert.assertNotNull(purchaseMerchant.getMerchantGuid());
    }

    @Test
    public void withoutMerchantGuid(){
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("PurchaseMerchant must have a merchantGuid when built");
        purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                .withCompanyName("cie")
                .withExternalReference("ref")
                .withMunicipality("city")
                .build();
    }




}
