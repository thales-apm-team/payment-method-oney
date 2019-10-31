package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.customer.PurchaseHistory;
import com.payline.payment.oney.bean.common.purchase.Purchase;
import com.payline.payment.oney.bean.common.purchase.Travel;
import com.payline.payment.oney.service.BeanAssembleService;
import com.payline.payment.oney.utils.TestUtils;
import com.payline.pmapi.bean.payment.BuyerExtendedHistory;
import com.payline.pmapi.bean.payment.Order;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.travel.OrderOTA;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class BeanAssemblerServiceImplTest {

    private BeanAssembleService beanAssembleService = BeanAssemblerServiceImpl.getInstance();

    @Test
    public void assembleCustomer(){
        // given: a payment request
        PaymentRequest paymentRequest = TestUtils.createDefaultPaymentRequest();

        // when: feeding a to assembleCustomer() method
        Customer customer = beanAssembleService.assembleCustomer( paymentRequest );

        // then: resulting object is not null
        assertNotNull( customer );
        assertNotNull( customer.getIdentity() );
    }

    @Test
    public void fromPaymentRequest() throws Exception {
        // given: a full payment request
        PaymentRequest paymentRequest = TestUtils.createCompletePaymentRequestBuilder().build();

        // when: feeding it to assemblePurchase() method
        Purchase purchase = beanAssembleService.assemblePurchase( paymentRequest );

        // then: item is not null and its string form contains some important field names
        assertNotNull( purchase );
        assertTrue(purchase.toString().contains("external_reference"));
        assertTrue(purchase.toString().contains("purchase_amount"));
        assertTrue(purchase.toString().contains("currency_code"));
        assertTrue(purchase.toString().contains("delivery"));
        assertTrue(purchase.toString().contains("item_list"));
    }

    @Test
    public void assemblePurchaseHistoryFull() {
        BuyerExtendedHistory buyerExtendedHistory = BuyerExtendedHistory.BuyerExtendedHistoryBuilder.aBuyerExtendedHistory()
                .withFirstOrderDate(new Date())
                .withLastOrderDate(new Date())
                .withTotalAmount(10500L)
                .withTotalCurrency("EUR")
                .build();

        // create paymentRequest
        PaymentRequest request = TestUtils.createCompletePaymentRequestBuilder()
                .withBuyer(
                        TestUtils.createCompleteBuyerBuilder()
                                .withBuyerExtendedHistory( buyerExtendedHistory )
                                .build()
                )
                .build();

        // test method
        PurchaseHistory purchaseHistory = beanAssembleService.assemblePurchaseHistory(request);

        //assertEquals(Integer.valueOf(10), purchaseHistory.getTotalNumberOfPurchase()); // disabled for PAYLAPMEXT-153
        assertEquals(Float.valueOf(105), purchaseHistory.getTotalAmount());
        assertNotNull(purchaseHistory.getFirstPurchaseDate());
        assertNotNull(purchaseHistory.getLastPurchaseDate());
    }

    @Test
    public void assemblePurchaseHistoryNoExtended()  {
        // create paymentRequest
        PaymentRequest request = TestUtils.createCompletePaymentRequestBuilder()
                .withBuyer(
                        TestUtils.createCompleteBuyerBuilder()
                                .withBuyerExtendedHistory(null)
                                .build()
                )
                .build();

        // test method
        PurchaseHistory purchaseHistory = beanAssembleService.assemblePurchaseHistory(request);

        assertNull(purchaseHistory.getTotalNumberOfPurchase());
        assertNull(purchaseHistory.getTotalAmount());
        assertNull(purchaseHistory.getFirstPurchaseDate());
        assertNull(purchaseHistory.getLastPurchaseDate());
    }

    /**
     * Test assembleTravel() method, but assembleJourneyList() and assembleStayList() as well.
     */
    @Test
    void assembleTravel() {
        // given: an Order, with an OrderOTA inside
        Order order = TestUtils.createCompleteOrder( TestUtils.createTransactionId() );
        assertNotNull( order.getOrderOTA() );

        // when: feeding it to assembleTravel() method
        Travel travel = beanAssembleService.assembleTravel( order );

        // then:
        OrderOTA orderOTA = order.getOrderOTA();
        assertNotNull( travel );
        assertNotNull( travel.getJourney() );
        assertEquals( order.getOrderOTA().getTransport().getLegList().size(), travel.getJourney().size() );
        assertNotNull( travel.getStay() );
    }

}
