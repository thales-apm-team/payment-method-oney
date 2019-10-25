package com.payline.payment.oney.service;

import com.payline.payment.oney.bean.common.NavigationData;
import com.payline.payment.oney.bean.common.customer.Customer;
import com.payline.payment.oney.bean.common.customer.CustomerIdentity;
import com.payline.payment.oney.bean.common.customer.PurchaseHistory;
import com.payline.payment.oney.bean.common.payment.BusinessTransactionData;
import com.payline.payment.oney.bean.common.payment.PaymentData;
import com.payline.payment.oney.bean.common.purchase.*;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.payment.Order;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.travel.OrderOTA;
import com.payline.pmapi.bean.travel.Transport;

import java.util.List;

public interface BeanAssembleService {

    BusinessTransactionData assembleBuisnessTransactionData(PaymentRequest paymentRequest) throws PluginTechnicalException;

    Customer assembleCustomer(PaymentRequest paymentRequest);

    CustomerIdentity assembleCustomerIdentity(Buyer buyer);

    Delivery assembleDelivery(PaymentRequest paymentRequest);

    List<Journey> assembleJourneyList(Transport transport);

    NavigationData assembleNavigationData(PaymentRequest paymentRequest) throws PluginTechnicalException;

    PaymentData assemblePaymentData(PaymentRequest paymentRequest, BusinessTransactionData businessTransaction) throws PluginTechnicalException;

    Purchase assemblePurchase(PaymentRequest paymentRequest);

    PurchaseHistory assemblePurchaseHistory(PaymentRequest paymentRequest);

    List<Stay> assembleStayList(OrderOTA orderOTA);

    Travel assembleTravel(Order order);
}
