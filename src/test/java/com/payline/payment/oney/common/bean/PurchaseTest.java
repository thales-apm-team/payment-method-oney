package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.purchase.Purchase;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.BeanUtils.createDelivery;
import static com.payline.payment.oney.utils.BeanUtils.createItemList;
import static com.payline.payment.oney.utils.BeanUtils.createPurchaseMerchant;
import static com.payline.payment.oney.utils.TestUtils.createCompletePaymentBuilder;

public class PurchaseTest {
    private Purchase purchase;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void purchase() {
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withPurchaseAmount(150f)
                .withDelivery(createDelivery())
                .withExternalReference("externalRef")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .withExternalReferenceType("CMDE")
                .build();

        System.out.println(purchase);
        Assert.assertNotNull(purchase);
    }

    //todo
    @Test
    public void fromPaymentRequest() {
        purchase = Purchase.Builder.aPurchaseBuilder()
                .fromPayline(createCompletePaymentBuilder().build())
                .build();
        System.out.println(purchase);

        //Test to String here
        Assert.assertTrue(purchase.toString().contains("external_reference"));
        Assert.assertTrue(purchase.toString().contains("purchase_amount"));
        Assert.assertTrue(purchase.toString().contains("currency_code"));
        Assert.assertTrue(purchase.toString().contains("delivery"));
        Assert.assertTrue(purchase.toString().contains("item_list"));
    }
}
