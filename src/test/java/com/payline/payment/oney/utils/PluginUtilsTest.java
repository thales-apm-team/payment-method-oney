package com.payline.payment.oney.utils;

import com.payline.payment.oney.bean.common.enums.PaymentType;
import com.payline.payment.oney.bean.common.purchase.Item;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Map;

import static com.payline.payment.oney.bean.common.enums.CategoryCodeHandler.findCategory;
import static com.payline.payment.oney.utils.PluginUtils.*;

public class PluginUtilsTest {


    @Test
    public void testTruncateText() {
        String longText = "Update: yes it doesn't take the slim characters into account but I don't agree with that considering everyone has different screens and fonts setup and a large portion of the people that land here on this page are probably looking for a maintained library like the above.";
        String longText2 = "Update:";
        Map textCutted = truncateLongText(longText, longText2, 19);
        Assert.assertTrue(textCutted.get("line1").toString().length() < 20);
        Assert.assertTrue(textCutted.get("line2") == null || textCutted.get("line2").toString().length() < 20);
        Assert.assertTrue(textCutted.get("line3") == null || textCutted.get("line3").toString().length() < 20);
        Assert.assertTrue(textCutted.get("line4") == null || textCutted.get("line4").toString().length() < 20);
        Assert.assertTrue(textCutted.get("line5") == null || textCutted.get("line5").toString().length() < 20);

    }

    @Test
    public void itemComparator() {
        ItemComparator comp = new ItemComparator();
        Item item1 = Item.Builder.aItemBuilder()
                .withMainItem(0)
                .withCategoryCode(0)
                .withLabel("label")
                .withPrice(110f)
                .withMarketplaceFlag(0)
                .withQuantity(5)
                .withItemExternalCode("externalCode")
                .build();
        Item item2 = Item.Builder.aItemBuilder()
                .withMainItem(0)
                .withCategoryCode(0)
                .withLabel("label")
                .withPrice(40f)
                .withMarketplaceFlag(0)
                .withQuantity(5)
                .withItemExternalCode("externalCode")
                .build();

        int highest = comp.compare(item1, item2);
        Assert.assertEquals(1, highest);
    }

    @Test
    public void testGetIsoAlpha3CodeFromCountryCode2() {
        String country = "FR";
        String pays = getIsoAlpha3CodeFromCountryCode2(country);
        Assert.assertEquals("FRA",pays);

    }

    @Test
    public void testGetCountryNameCodeFromCountryCode2() {
        String country = "FR";
        String pays = getCountryNameCodeFromCountryCode2(country);
        Assert.assertEquals("France",pays);



    }


    @Test
    public void testCategoryHandler(){
        int catCodePayline = 17;
        int catCodePayline2 = 964;
        int catCodeOney = findCategory(catCodePayline);
        int catCodeOney2 = findCategory(catCodePayline2);

        Assert.assertEquals(1, catCodeOney);
        Assert.assertEquals(5, catCodeOney2);

        System.out.println(PaymentType.CHECK_CARD.getValue());
    }
}
