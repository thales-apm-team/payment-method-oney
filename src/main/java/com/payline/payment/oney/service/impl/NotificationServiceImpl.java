package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.i18n.I18nService;
import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import com.payline.pmapi.bean.payment.request.NotifyTransactionStatusRequest;
import com.payline.pmapi.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentWithRedirectionServiceImpl.class);
    private OneyHttpClient httpClient;
    private I18nService i18n = I18nService.getInstance();

    @Override
    public NotificationResponse parse(NotificationRequest notificationRequest) {
        return new IgnoreNotificationResponse();
    }

    @Override
    public void notifyTransactionStatus(NotifyTransactionStatusRequest notifyTransactionStatusRequest) {
        //ras.

    }
}
