package com.payline.payment.oney.common.bean;

import com.payline.payment.oney.bean.common.purchase.Item;
import com.payline.payment.oney.bean.common.purchase.Travel;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.payline.payment.oney.utils.TestUtils.createAmount;
import static com.payline.payment.oney.utils.TestUtils.createOrderItem;

public class ItemTest {

    private Item item;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void item() {
        item = Item.Builder.aItemBuilder()
                .withMainItem(0)
                .withCategoryCode(0)
                .withLabel("label")
                .withPrice(new Float(175))
                .withMarketplaceFlag(0)
                .withQuantity(5)
                .withItemExternalCode("externalCode")
                .build();
        Assert.assertNotNull(item.getIsMainItem());
        Assert.assertNotNull(item.getItemExternalcode());
        Assert.assertNotNull(item.getCategoryCode());
        Assert.assertNotNull(item.getLabel());
        Assert.assertNotNull(item.getPrice());
        Assert.assertNotNull(item.getMarketplaceFlag());
        Assert.assertNotNull(item.getQuantity());
    }

    @Test
    public void itemFromPaylineRequest() {
        item = Item.Builder.aItemBuilder()
                .fromPayline(createOrderItem("someRefe", createAmount("EUR")))
                .build();

        Assert.assertNotNull(item.getIsMainItem());
        Assert.assertNotNull(item.getItemExternalcode());
        Assert.assertNotNull(item.getCategoryCode());
        Assert.assertNotNull(item.getLabel());
        Assert.assertNotNull(item.getPrice());
        Assert.assertNotNull(item.getMarketplaceFlag());
        Assert.assertNotNull(item.getQuantity());
    }

    @Test
    public void testToString() {
        item = Item.Builder.aItemBuilder()
                .fromPayline(createOrderItem("someRefe", createAmount("EUR")))
                .withTravel(new Travel())
                .build();

        Assert.assertTrue(item.toString().contains("is_main_item"));
        Assert.assertTrue(item.toString().contains("category_code"));
        Assert.assertTrue(item.toString().contains("label"));
        Assert.assertTrue(item.toString().contains("item_external_code"));
        Assert.assertTrue(item.toString().contains("quantity"));
        Assert.assertTrue(item.toString().contains("price"));
        System.out.println(item);


    }
    @Test
    public void defineMainItem() {
       //to implement
        List<Item> itemList = new ArrayList<>();
        itemList.add(Item.Builder.aItemBuilder()
                .withMainItem(0)
                .withCategoryCode(0)
                .withLabel("label1")
                .withPrice(10f)
                .withMarketplaceFlag(0)
                .withQuantity(5)
                .withItemExternalCode("externalCode")
                .build());
        itemList.add(Item.Builder.aItemBuilder()
                .withMainItem(0)
                .withCategoryCode(0)
                .withLabel("label2")
                .withPrice(25f)
                .withMarketplaceFlag(0)
                .withQuantity(5)
                .withItemExternalCode("externalCode")
                .build());
        itemList.add(Item.Builder.aItemBuilder()
                .withMainItem(0)
                .withCategoryCode(0)
                .withLabel("label3")
                .withPrice(15f)
                .withMarketplaceFlag(0)
                .withQuantity(5)
                .withItemExternalCode("externalCode")
                .build());


        Item.defineMainItem(itemList);
        Assert.assertEquals(0, (int) itemList.get(0).getIsMainItem());
        Assert.assertEquals(1, (int) itemList.get(1).getIsMainItem());
        Assert.assertEquals(0, (int) itemList.get(2).getIsMainItem());
    }

}
