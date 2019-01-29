package com.payline.payment.oney.bean.common;

import com.payline.payment.oney.bean.common.purchase.PurchaseMerchant;
import com.payline.payment.oney.exception.InvalidDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.utils.TestUtils.createCompletePaymentBuilder;

public class PurchaseMerchantTest {

    private PurchaseMerchant purchaseMerchant;


    @Test
    public void purchaseMerchantOK() throws Exception {
        purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                .withCompanyName("cie")
                .withExternalReference("ref")
                .withMerchantGuid("guid")
                .withMunicipality("city")
                .build();

        Assertions.assertNotNull(purchaseMerchant);
        Assertions.assertNotNull(purchaseMerchant.getExternalReference());
        Assertions.assertNotNull(purchaseMerchant.getMerchantGuid());
        Assertions.assertTrue(purchaseMerchant.toString().contains("company_name"));
        Assertions.assertTrue(purchaseMerchant.toString().contains("external_reference"));
        Assertions.assertTrue(purchaseMerchant.toString().contains("merchant_guid"));
        Assertions.assertTrue(purchaseMerchant.toString().contains("municipality"));
    }


    @Test
    public void withoutExternalRef() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                    .withCompanyName("cie")
                    .withMerchantGuid("guid")
                    .withMunicipality("city")
                    .build();

        });
        Assertions.assertEquals("PurchaseMerchant must have a externalReference when built", exception.getMessage());

    }

    @Test
    public void withoutMerchantGuid() {
        Throwable exception = Assertions.assertThrows(InvalidDataException.class, () -> {
            purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                    .withCompanyName("cie")
                    .withExternalReference("ref")
                    .withMunicipality("city")
                    .build();
        });
        Assertions.assertEquals("PurchaseMerchant must have a merchantGuid when built", exception.getMessage());

    }

    @Test
    public void fromPayline() throws Exception {

        purchaseMerchant = PurchaseMerchant.Builder.aPurchaseMerchantBuilder()
                .fromPayline(createCompletePaymentBuilder().build())
                .build();

        Assertions.assertNotNull(purchaseMerchant.getExternalReference());
        Assertions.assertNotNull(purchaseMerchant.getMerchantGuid());
    }


}
