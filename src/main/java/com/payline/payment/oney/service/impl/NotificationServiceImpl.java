package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.common.PurchaseNotification;
import com.payline.payment.oney.bean.request.OneyConfirmRequest;
import com.payline.payment.oney.bean.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.bean.response.OneyNotificationResponse;
import com.payline.payment.oney.bean.response.TransactionStatusResponse;
import com.payline.payment.oney.exception.HttpCallException;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.OneyErrorHandler;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.*;
import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.PaymentResponseByNotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.TransactionStateChangedResponse;
import com.payline.pmapi.bean.payment.request.NotifyTransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.EmptyTransactionDetails;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseOnHold;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import static com.payline.payment.oney.bean.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
import static com.payline.payment.oney.utils.OneyConstants.HTTP_OK;

public class NotificationServiceImpl implements NotificationService {
    private static final Logger LOGGER = LogManager.getLogger(NotificationServiceImpl.class);
    private OneyHttpClient httpClient;

    public NotificationServiceImpl() {
        httpClient = OneyHttpClient.getInstance();
    }

    @Override
    public NotificationResponse parse(NotificationRequest request) {
        NotificationResponse notificationResponse;
        final String transactionId = request.getTransactionId();

        TransactionCorrelationId correlationId = TransactionCorrelationId.TransactionCorrelationIdBuilder
                .aCorrelationIdBuilder()
                .withType(TransactionCorrelationId.CorrelationIdType.TRANSACTION_ID)
                .withValue(transactionId == null ? "UNKNOWN" : transactionId)
                .build();
        try {
            // init data
            final String key = RequestConfigServiceImpl.INSTANCE.getParameterValue(request, OneyConstants.PARTNER_CHIFFREMENT_KEY);

            // read body from request
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getContent(), StandardCharsets.UTF_8));
            String bodyResponse = br.lines().collect(Collectors.joining(System.lineSeparator()));

            // create an OneyResponse object from json String
            OneyNotificationResponse oneyResponse = OneyNotificationResponse.createTransactionStatusResponseFromJson(bodyResponse, key);


            if (transactionId == null) {
                // if transaction doesn't already exists return a PaymentResponseByNotificationResponse
                notificationResponse = getPaymentResponseByNotificationResponseFromNotificationRequest(request, oneyResponse);

            } else if (transactionId.equals(oneyResponse.getPspContext())) {
                // if transaction already exists return a TransactionStatusChanged
                notificationResponse = getTransactionStatusChangedFromNotificationRequest(request, oneyResponse);

            } else {
                // transaction is not null but isn't equal to received transactionId
                LOGGER.info("Given transactionId doesn't match psp_context");
                throw new InvalidDataException("Given transactionId doesn't match psp_context");
            }

        } catch (PluginTechnicalException e) {
            notificationResponse = PaymentResponseByNotificationResponse.PaymentResponseByNotificationResponseBuilder.aPaymentResponseByNotificationResponseBuilder()
                    .withPaymentResponse(e.toPaymentResponseFailure())
                    .withTransactionCorrelationId(correlationId)
                    .withHttpStatus(204)
                    .build();

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected plugin error", e);
            PaymentResponse paymentResponse = PaymentResponseFailure.PaymentResponseFailureBuilder
                    .aPaymentResponseFailure()
                    .withErrorCode(PluginTechnicalException.runtimeErrorCode(e))
                    .withFailureCause(FailureCause.INTERNAL_ERROR)
                    .build();

            notificationResponse = PaymentResponseByNotificationResponse.PaymentResponseByNotificationResponseBuilder.aPaymentResponseByNotificationResponseBuilder()
                    .withPaymentResponse(paymentResponse)
                    .withTransactionCorrelationId(correlationId)
                    .withHttpStatus(204)
                    .build();

        }
        return notificationResponse;
    }

    private NotificationResponse getPaymentResponseByNotificationResponseFromNotificationRequest(NotificationRequest request, OneyNotificationResponse oneyResponse) throws PluginTechnicalException {
        PaymentResponse paymentResponse;

        // extract infos from oneyResponse
        PurchaseNotification purchase = oneyResponse.getPurchase();
        String partnerTransactionId = purchase.getExternalReference();
        String paymentStatus = purchase.getStatusCode();
        Boolean isCaptureNow = PluginUtils.isCaptureNow(oneyResponse.getMerchantContext());

        TransactionCorrelationId transactionCorrelationId = TransactionCorrelationId.TransactionCorrelationIdBuilder.aCorrelationIdBuilder()
                .withType(TransactionCorrelationId.CorrelationIdType.PARTNER_TRANSACTION_ID)
                .withValue(partnerTransactionId)
                .build();

//        try {
        // create a template success response
        PaymentResponse successPaymentResponse = createSuccessPaymentResponse(partnerTransactionId, purchase.getStatusLabel());

        // get PaymentResponseFrom Oney paymentStatus
        switch (paymentStatus) {
            case PurchaseNotification.ValidStatus.FUNDED:
            case PurchaseNotification.ValidStatus.TO_BE_FUNDED:
            case PurchaseNotification.ValidStatus.CANCELLED:
                paymentResponse = successPaymentResponse;
                break;

            case PurchaseNotification.ValidStatus.FAVORABLE:
                // if captureNow => do the comfirm call
                if (Boolean.TRUE.equals(isCaptureNow)) {
                    paymentResponse = getConfirmationPaymentResponse(request, oneyResponse);
                } else {
                    // is NOT to capture now
                    paymentResponse = successPaymentResponse;
                }

                break;
            case PurchaseNotification.ValidStatus.REFUSED:
                paymentResponse = OneyErrorHandler.getPaymentResponseFailure(FailureCause.REFUSED,
                        partnerTransactionId, PluginUtils.truncate(oneyResponse.getPurchase().getStatusLabel(), 50));
                break;
            case PurchaseNotification.ValidStatus.ABORTED:
                paymentResponse = OneyErrorHandler.getPaymentResponseFailure(FailureCause.CANCEL,
                        partnerTransactionId, PluginUtils.truncate(oneyResponse.getPurchase().getStatusLabel(), 50));
                break;
            case PurchaseNotification.ValidStatus.PENDING:
                paymentResponse = PaymentResponseOnHold.PaymentResponseOnHoldBuilder.aPaymentResponseOnHold()
                        .withPartnerTransactionId(partnerTransactionId)
                        .withOnHoldCause(OnHoldCause.SCORING_ASYNC)
                        .build();
                break;
            default:
                // Ignore the notification, with a 204 HTTP status code
                LOGGER.info("Unknown payment status: " + paymentStatus);
                return IgnoreNotificationResponse.IgnoreNotificationResponseBuilder.aIgnoreNotificationResponseBuilder()
                        .withHttpStatus(204)
                        .build();
        }

        return PaymentResponseByNotificationResponse.PaymentResponseByNotificationResponseBuilder.aPaymentResponseByNotificationResponseBuilder()
                .withPaymentResponse(paymentResponse)
                .withTransactionCorrelationId(transactionCorrelationId)
                .withHttpStatus(204)
                .build();
    }

    private PaymentResponse getConfirmationPaymentResponse(NotificationRequest request, OneyNotificationResponse oneyResponse) throws PluginTechnicalException {
        try {
            String status = confirmAndCheck(request, oneyResponse);

            if ("FUNDED".equals(status) || "TO_BE_FUNDED".equals(status)) {
                // success
                return createSuccessPaymentResponse(
                        oneyResponse.getPurchase().getExternalReference()
                        , oneyResponse.getPurchase().getStatusLabel());
            } else {
                return OneyErrorHandler.getPaymentResponseFailure(
                        FailureCause.REFUSED
                        , oneyResponse.getPurchase().getExternalReference()
                        , "payment not funded");
            }


        } catch (HttpCallException e) {
            return OneyErrorHandler.getPaymentResponseFailure(
                    e.getFailureCause()
                    , oneyResponse.getPurchase().getExternalReference()
                    , PluginUtils.truncate(e.getErrorCodeOrLabel(), 50)
            );
        }
    }


    private NotificationResponse getTransactionStatusChangedFromNotificationRequest(NotificationRequest request, OneyNotificationResponse oneyResponse) {
        NotificationResponse response;
        String partnerTransactionId = "UNKNOWN";
        final String transactionId = request.getTransactionId();

        try {

            // extract infos from oneyResponse
            partnerTransactionId = oneyResponse.getPurchase().getExternalReference();
            String paymentStatus = oneyResponse.getPurchase().getStatusCode();
            Boolean isCaptureNow = PluginUtils.isCaptureNow(oneyResponse.getMerchantContext());

            // get transactionStatus from Oney paymentStatus
            TransactionStatus status;
            switch (paymentStatus) {
                case PurchaseNotification.ValidStatus.FUNDED:
                case PurchaseNotification.ValidStatus.TO_BE_FUNDED:
                case PurchaseNotification.ValidStatus.CANCELLED:
                    status = new SuccessTransactionStatus();
                    break;
                case PurchaseNotification.ValidStatus.FAVORABLE:
                    //
                    if (Boolean.FALSE.equals(isCaptureNow)) {
                        // confirm later
                        status = new SuccessTransactionStatus();
                    } else {
                        // confirm now
                        status = getConfirmationStatus(request, oneyResponse);
                    }
                    break;
                case PurchaseNotification.ValidStatus.REFUSED:
                case PurchaseNotification.ValidStatus.ABORTED:
                    status = new FailureTransactionStatus(FailureCause.REFUSED);
                    break;
                case PurchaseNotification.ValidStatus.PENDING:
                    status = new OnHoldTransactionStatus(OnHoldCause.SCORING_ASYNC);
                    break;
                default:
                    // Ignore the notification, with a 204 HTTP status code
                    LOGGER.info("Unknown payment status: " + paymentStatus);
                    return IgnoreNotificationResponse.IgnoreNotificationResponseBuilder
                            .aIgnoreNotificationResponseBuilder()
                            .withHttpStatus(204)
                            .build();
            }

            PurchaseNotification purchase = oneyResponse.getPurchase();
            String statusDetails = purchase == null ? null : purchase.getReasonCode() + "-" + purchase.getReasonLabel();
            response = TransactionStateChangedResponse.TransactionStateChangedResponseBuilder
                    .aTransactionStateChangedResponse()
                    .withPartnerTransactionId(partnerTransactionId)
                    .withTransactionId(transactionId)
                    .withTransactionStatus(status)
                    .withStatusDate(new Date())
                    .withHttpStatus(204)
                    .withAction(isCaptureNow ? TransactionStateChangedResponse.Action.AUTHOR_AND_CAPTURE : TransactionStateChangedResponse.Action.AUTHOR)
                    .withStatusDetails(statusDetails)
                    .build();

        } catch (PluginTechnicalException e) {
            response = TransactionStateChangedResponse.TransactionStateChangedResponseBuilder
                    .aTransactionStateChangedResponse()
                    .withPartnerTransactionId(partnerTransactionId)
                    .withTransactionId(transactionId)
                    .withTransactionStatus(new FailureTransactionStatus(e.getFailureCause()))
                    .withStatusDate(new Date())
                    .withHttpStatus(204)
                    .build();

        } catch (RuntimeException e) {
            LOGGER.error("Unexpected plugin error", e);

            response = TransactionStateChangedResponse.TransactionStateChangedResponseBuilder
                    .aTransactionStateChangedResponse()
                    .withPartnerTransactionId(partnerTransactionId)
                    .withTransactionId(transactionId)
                    .withTransactionStatus(new FailureTransactionStatus(FailureCause.INTERNAL_ERROR))
                    .withStatusDate(new Date())
                    .withHttpStatus(204)
                    .build();
        }
        return response;
    }


    private TransactionStatus getConfirmationStatus(NotificationRequest request, OneyNotificationResponse oneyResponse) throws PluginTechnicalException {
        try{
            String status  = confirmAndCheck(request, oneyResponse);
            if ("FUNDED".equals(status) || "TO_BE_FUNDED".equals(status)) {
                // success
                return new SuccessTransactionStatus();
            } else {
                LOGGER.error("Unable to read the confirmation response transaction status");
                return new FailureTransactionStatus(FailureCause.REFUSED);
            }
        }catch (HttpCallException e){
            return new FailureTransactionStatus(e.getFailureCause());
        }
    }

    private String confirmAndCheck(NotificationRequest request, OneyNotificationResponse oneyResponse) throws PluginTechnicalException {
        final String key = RequestConfigServiceImpl.INSTANCE.getParameterValue(request, OneyConstants.PARTNER_CHIFFREMENT_KEY);

        // confirmation
        OneyConfirmRequest confirmRequest = new OneyConfirmRequest.Builder(request, oneyResponse).build();
        httpClient.initiateConfirmationPayment(confirmRequest, request.getEnvironment().isSandbox());


        // check
        OneyTransactionStatusRequest oneyTransactionStatusRequest = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .fromNotificationRequest(request)
                .withPurchaseReference(PluginUtils.fullPurchaseReference(oneyResponse.getPurchase().getExternalReference()))
                .build();
        StringResponse checkStatusResponse = httpClient.initiateGetTransactionStatus(oneyTransactionStatusRequest, request.getEnvironment().isSandbox());


        // verify the checkResponse
        if (checkStatusResponse.getContent() == null) {
            String message = "Unable to read the check response";
            LOGGER.error(message);
            throw new HttpCallException(message, "empty check response content");
        }
        if ( checkStatusResponse.getCode() != HTTP_OK) {
            String message = "bad response to the check request";
            LOGGER.error(message);
            throw new InvalidDataException(message, "bad responseCode");
        }


        TransactionStatusResponse confirmTransactionResponse = createTransactionStatusResponseFromJson(checkStatusResponse.getContent(), key);
        if (confirmTransactionResponse == null || confirmTransactionResponse.getStatusPurchase() == null) {
            // unable to read the payment status
            String message = "Unable to read the confirmation response transaction status";
            LOGGER.error(message);
            throw new HttpCallException(message, "empty check response object or statusCode");
        }

        // check the confirmation response status
        return confirmTransactionResponse.getStatusPurchase().getStatusCode();
    }


    PaymentResponse createSuccessPaymentResponse(String partnerTransactionId, String message) {
        return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                .withStatusCode(Integer.toString(HTTP_OK))
                .withTransactionDetails(new EmptyTransactionDetails())
                .withPartnerTransactionId(partnerTransactionId)
                .withMessage(new Message(Message.MessageType.SUCCESS, message))
                .build();
    }


    @Override
    public void notifyTransactionStatus(NotifyTransactionStatusRequest notifyTransactionStatusRequest) {
        //ras.
    }
}
