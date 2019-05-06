package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.response.OneyNotificationResponse;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.FailureTransactionStatus;
import com.payline.pmapi.bean.common.SuccessTransactionStatus;
import com.payline.pmapi.bean.common.TransactionStatus;
import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.TransactionStateChangedResponse;
import com.payline.pmapi.bean.payment.request.NotifyTransactionStatusRequest;
import com.payline.pmapi.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.stream.Collectors;

public class NotificationServiceImpl implements NotificationService {
    private static final Logger LOGGER = LogManager.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationResponse parse(NotificationRequest notificationRequest) {
        // init data
        String transactionId = "UNKNOWN";
        String partnerTransactionId = "UNKNOWN";
        TransactionStateChangedResponse.Action action = TransactionStateChangedResponse.Action.AUTHOR_AND_CAPTURE;
        TransactionStatus status = new FailureTransactionStatus();

        // get the body of the request
        try (BufferedReader br = new BufferedReader(new InputStreamReader(notificationRequest.getContent(), "UTF-8"))) {
            String bodyResponse = br.lines().collect(Collectors.joining(System.lineSeparator()));
            final String key = "";  // unable to get the true key (see PAYLAPMEXT-150)

            // extract all needed data
            OneyNotificationResponse oneyResponse = OneyNotificationResponse.createTransactionStatusResponseFromJson(bodyResponse, key);
            transactionId = oneyResponse.getPspContext();
            partnerTransactionId = oneyResponse.getPurchase().getExternalReference();


            // check the status
            String paymentStatus = oneyResponse.getPurchase().getStatusLabel();
            if ("FUNDED".equals(paymentStatus)) {
                status = new SuccessTransactionStatus();
            } else if ("REFUSED".equals(paymentStatus) || "ABORTED".equals(paymentStatus)) {
                status = new FailureTransactionStatus(FailureCause.REFUSED);
            } else {
                return new IgnoreNotificationResponse();
            }


        } catch (IOException e) {
            LOGGER.error("Unable to read the notification request's body");
        } catch (PluginTechnicalException e) {
            LOGGER.error("Unable to get infomation needed to know the transaction status");
        }

        return TransactionStateChangedResponse.TransactionStateChangedResponseBuilder.aTransactionStateChangedResponse()
                .withTransactionId(transactionId)
                .withPartnerTransactionId(partnerTransactionId)
                .withStatusDate(new Date())
                .withAction(action)
                .withTransactionStatus(status)
                .build();
    }

    @Override
    public void notifyTransactionStatus(NotifyTransactionStatusRequest notifyTransactionStatusRequest) {
        //ras.

    }
}
