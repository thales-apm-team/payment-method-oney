package com.payline.payment.oney.utils;

import com.payline.payment.oney.bean.common.purchase.Item;
import com.payline.payment.oney.bean.common.purchase.Purchase;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.InvalidFieldFormatException;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigInteger;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.oney.bean.common.enums.CategoryCodeHandler.findCategory;
import static com.payline.payment.oney.utils.BeanUtils.*;
import static com.payline.payment.oney.utils.OneyConstants.*;
import static com.payline.payment.oney.utils.PluginUtils.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PluginUtilsTest {


    String merchantId1;

    @BeforeAll
    public void setUp() {
        merchantId1 = generateMerchantRequestId("merchantId");
    }

    @Test
    public void testTruncateText() {
        String longText = "Update: yes it doesn't take the slim characters into account but I don't agree with that considering everyone has different screens and fonts setup and a large portion of the people that land here on this page are probably looking for a maintained library like the above.";
        String longText2 = "Update:";
        Map textCutted = truncateLongText(longText, longText2, 19);
        Assertions.assertTrue(textCutted.get("line1").toString().length() < 20);
        Assertions.assertTrue(textCutted.get("line2") == null || textCutted.get("line2").toString().length() < 20);
        Assertions.assertTrue(textCutted.get("line3") == null || textCutted.get("line3").toString().length() < 20);
        Assertions.assertTrue(textCutted.get("line4") == null || textCutted.get("line4").toString().length() < 20);
        Assertions.assertTrue(textCutted.get("line5") == null || textCutted.get("line5").toString().length() < 20);

    }

    @Test
    public void itemComparator() throws Exception {
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
        Assertions.assertEquals(1, highest);
    }

    @Test
    public void testGetIsoAlpha3CodeFromCountryCode2() {
        String country = "FR";
        String pays = getIsoAlpha3CodeFromCountryCode2(country);
        Assertions.assertEquals("FRA", pays);

    }

    @Test
    public void testGetCountryNameCodeFromCountryCode2() {
        String country = "FR";
        String pays = getCountryNameCodeFromCountryCode2(country);
        Assertions.assertEquals("France", pays);


    }


    @Test
    public void testCategoryHandler() {
        String catCodePayline = "17";
        String catCodePayline2 = "964";
        int catCodeOney = findCategory(catCodePayline);
        int catCodeOney2 = findCategory(catCodePayline2);

        Assertions.assertEquals(1, catCodeOney);
        Assertions.assertEquals(5, catCodeOney2);

    }

    @Test
    public void testDeliveryModeCode() {
        String catCodePayline = "1";
        String catCodePayline2 = "4";
        int catCodeOney = getOneyDeliveryModeCode(catCodePayline);
        int catCodeOney2 = getOneyDeliveryModeCode(catCodePayline2);

        Assertions.assertEquals(1, catCodeOney);
        Assertions.assertEquals(4, catCodeOney2);
    }

    @Test
    public void testDeliveryOption() {
        String mode1 = "1";
        String mode2 = "2";
        String mode3 = "17";
        int catCodeOney = getOneyDeliveryOption(mode1);
        int catCodeOney2 = getOneyDeliveryOption(mode2);
        int catCodeOney3 = getOneyDeliveryOption(mode3);

        Assertions.assertEquals(1, catCodeOney);
        Assertions.assertEquals(2, catCodeOney2);
        Assertions.assertEquals(2, catCodeOney3);
    }

    @Test
    public void testHonorificName() {
        String mr = "4";
        String mme = "1";
        String miss = "3";
        int hCodeOney = getHonorificCode(mr);
        int hCodeOney2 = getHonorificCode(mme);
        int hCodeOney3 = getHonorificCode(miss);

        Assertions.assertEquals(1, hCodeOney);
        Assertions.assertEquals(2, hCodeOney2);
        Assertions.assertEquals(3, hCodeOney3);

    }

    @Test
    public void createStringAmount() {
        BigInteger int1 = BigInteger.ZERO;
        BigInteger int2 = BigInteger.ONE;
        BigInteger int3 = BigInteger.TEN;
        BigInteger int4 = BigInteger.valueOf(100);
        BigInteger int5 = BigInteger.valueOf(1000);

        Assertions.assertEquals("0.00", PluginUtils.createStringAmount(int1, Currency.getInstance("EUR")));
        Assertions.assertEquals("0.01", PluginUtils.createStringAmount(int2, Currency.getInstance("EUR")));
        Assertions.assertEquals("0.10", PluginUtils.createStringAmount(int3, Currency.getInstance("EUR")));
        Assertions.assertEquals("1.00", PluginUtils.createStringAmount(int4, Currency.getInstance("EUR")));
        Assertions.assertEquals("10.00", PluginUtils.createStringAmount(int5, Currency.getInstance("EUR")));
    }

    @Test
    public void createFloatAmount() {
        BigInteger int1 = BigInteger.ZERO;
        BigInteger int2 = BigInteger.ONE;
        BigInteger int3 = BigInteger.TEN;
        BigInteger int4 = BigInteger.valueOf(100);
        BigInteger int5 = BigInteger.valueOf(1000);

        Assertions.assertEquals(new Float("00.00"), PluginUtils.createFloatAmount(int1, Currency.getInstance("EUR")));
        Assertions.assertEquals(new Float("00.01"), PluginUtils.createFloatAmount(int2, Currency.getInstance("EUR")));
        Assertions.assertEquals(new Float("00.10"), PluginUtils.createFloatAmount(int3, Currency.getInstance("EUR")));
        Assertions.assertEquals(new Float("1.00"), PluginUtils.createFloatAmount(int4, Currency.getInstance("EUR")));
        Assertions.assertEquals(new Float("10.00"), PluginUtils.createFloatAmount(int5, Currency.getInstance("EUR")));
    }

    @Test
    public void testIsISO639() {
        Assertions.assertFalse(isISO639(new ContractProperty("FR")));
        Assertions.assertTrue(isISO639(new ContractProperty("fr")));
    }

    @Test
    public void testIsISO3166() {
        Assertions.assertFalse(isISO3166(new ContractProperty("FRA")));
        Assertions.assertTrue(isISO3166(new ContractProperty("FR")));
    }

    @Test
    public void getParameters_noCoutryCode() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {

            PartnerConfiguration partnerConfiguration =
                    new PartnerConfiguration(new HashMap<String, String>(), new HashMap<String, String>());
            getParametersMap(partnerConfiguration, null);

        });

    }

    @Test
    public void getParameters_emptyCoutryCode() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {

            PartnerConfiguration partnerConfiguration =
                    new PartnerConfiguration(new HashMap<String, String>(), new HashMap<String, String>());
            getParametersMap(partnerConfiguration, "");

        });

    }

    @Test
    public void getParameters_noAuthorizationKey() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {

            Map<String, String> params = new HashMap<String, String>();
            params.put(PARTNER_API_URL, "PARTNER_API_URL");

            PartnerConfiguration partnerConfiguration =
                    new PartnerConfiguration(params, new HashMap<String, String>());
            getParametersMap(partnerConfiguration, "coutryCode");

        });

    }

    @Test
    public void getParameters_noPartnerUrl() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {

            Map<String, String> params = new HashMap<String, String>();
            params.put(PARTNER_AUTHORIZATION_KEY, "PARTNER_AUTHORIZATION_KEY");

            PartnerConfiguration partnerConfiguration =
                    new PartnerConfiguration(params, new HashMap<String, String>());
            getParametersMap(partnerConfiguration, "coutryCode");

        });

    }

    @Test
    public void getParameters_ok() throws Exception {

        Map<String, String> params = new HashMap<String, String>();
        params.put(PARTNER_AUTHORIZATION_KEY, "PARTNER_AUTHORIZATION_KEY");
        params.put(PARTNER_API_URL, "PARTNER_API_URL");

        PartnerConfiguration partnerConfiguration =
                new PartnerConfiguration(params, new HashMap<String, String>());
        Map<String, String> result = getParametersMap(partnerConfiguration, "coutryCode");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.containsKey(PARTNER_AUTHORIZATION_KEY));
        Assertions.assertTrue(result.containsKey(PARTNER_API_URL));
        Assertions.assertTrue(result.containsKey(HEADER_COUNTRY_CODE));

    }

    @Test
    public void testGenerateReference() throws Exception {
        String expected = "external_reference_type%7Cexternal_reference";
        Purchase purchase = Purchase.Builder.aPurchaseBuilder()
                .withPurchaseMerchant(createPurchaseMerchant())
                .withCurrencyCode("EUR")
                .withPurchaseAmount(150f)
                .withDelivery(createDelivery())
                .withExternalReference("external_reference")
                .withListItem(createItemList())
                .withNumberOfItems(2)
                .withExternalReferenceType("external_reference_type")
                .build();
        String result = generateReference(purchase);
        Assertions.assertEquals(expected, result);
    }


    @Test
    public void parseReference_noPipe() {
        Throwable exception = Assertions.assertThrows(InvalidFieldFormatException.class, () -> {


            parseReference("test#test");

        });

    }


    @Test
    public void parseReference_emptyReference() {
        Throwable exception = Assertions.assertThrows(InvalidFieldFormatException.class, () -> {


            parseReference("");

        });

    }


    @Test
    public void parseReference_nullReference() {
        Throwable exception = Assertions.assertThrows(InvalidFieldFormatException.class, () -> {


            parseReference(null);

        });

    }

    @Test
    public void testParseReference() throws InvalidFieldFormatException {
        String ref = parseReference("xxx%7Ctest");
        Assertions.assertEquals("test", ref);
    }

    @Test
    public void testGenerateMerchantRequestId() {

        String merchantId2 = generateMerchantRequestId("merchantId");
        Assertions.assertNotEquals(merchantId1, merchantId2);
    }


    @Test
    public void truncate() {
        Assertions.assertEquals("0123456789", PluginUtils.truncate("01234567890123456789", 10));
        Assertions.assertEquals("01234567890123456789", PluginUtils.truncate("01234567890123456789", 60));
        Assertions.assertEquals("", PluginUtils.truncate("", 30));
        Assertions.assertNull(PluginUtils.truncate(null, 30));
    }
}
