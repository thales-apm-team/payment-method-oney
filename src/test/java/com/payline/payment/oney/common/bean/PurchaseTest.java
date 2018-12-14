package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.purchase.Purchase;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.payline.payment.oney.utils.BeanUtils.*;
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

        Assert.assertNotNull(purchase);
    }


    @Test
    public void fromPaymentRequest() {
        purchase = Purchase.Builder.aPurchaseBuilder()
                .fromPayline(createCompletePaymentBuilder().build())
                .build();

        //Test to String here
        Assert.assertTrue(purchase.toString().contains("external_reference"));
        Assert.assertTrue(purchase.toString().contains("purchase_amount"));
        Assert.assertTrue(purchase.toString().contains("currency_code"));
        Assert.assertTrue(purchase.toString().contains("delivery"));
        Assert.assertTrue(purchase.toString().contains("item_list"));
    }

    @Test
    public void withoutListItem() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Purchase must have a listItem when built");
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withPurchaseAmount(150f)
                .withDelivery(createDelivery())
                .withExternalReference("externalRef")
                .withNumberOfItems(2)
                .withExternalReferenceType("CMDE")
                .build();

    }

    @Test
    public void withoutDelivery() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Purchase must have a delivery when built");
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withPurchaseAmount(150f)
                .withExternalReference("externalRef")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .withExternalReferenceType("CMDE")
                .build();

    }

    @Test
    public void withoutExternalReferenceType() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Purchase must have a externalReferenceType when built");
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withPurchaseAmount(150f)
                .withDelivery(createDelivery())
                .withExternalReference("externalRef")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .build();

    }

    @Test
    public void withoutExternalReference() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Purchase must have a externalReference when built");
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withPurchaseAmount(150f)
                .withDelivery(createDelivery())
                .withExternalReferenceType("CMDE")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .build();

    }

    @Test
    public void withoutPuchaseAmount() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Purchase must have a purchaseAmount when built");
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withDelivery(createDelivery())
                .withExternalReference("externalRef")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .withExternalReferenceType("CMDE")
                .build();

    }

    @Test
    public void withoutCurrencyCode() {
        expectedEx.expect(IllegalStateException.class);
        expectedEx.expectMessage("Purchase must have a currencyCode when built");
        purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withPurchaseAmount(150f)
                .withDelivery(createDelivery())
                .withExternalReference("externalRef")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .withExternalReferenceType("CMDE")
                .build();

    }

}
