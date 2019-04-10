package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.common.PurchaseStatus;
import com.payline.payment.oney.bean.request.OneyConfirmRequest;
import com.payline.payment.oney.bean.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.bean.response.OneyFailureResponse;
import com.payline.payment.oney.bean.response.TransactionStatusResponse;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.MalformedResponseException;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.OneyErrorHandler;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.common.OnHoldCause;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.EmptyTransactionDetails;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseOnHold;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.logger.LogManager;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import org.apache.logging.log4j.Logger;

import static com.payline.payment.oney.bean.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.bean.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
import static com.payline.payment.oney.utils.OneyConstants.HTTP_OK;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;

public class PaymentWithRedirectionServiceImpl implements PaymentWithRedirectionService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentWithRedirectionServiceImpl.class);
    private static final int MAX_ITERATION = 15;
    private OneyHttpClient httpClient;

    private static final String ERROR_CODE = "Purchase status : ";

    public PaymentWithRedirectionServiceImpl() {
        this.httpClient = OneyHttpClient.getInstance();
    }

    @Override
    public PaymentResponse finalizeRedirectionPayment(RedirectionPaymentRequest redirectionPaymentRequest) {
        try {
            OneyConfirmRequest confirmRequest = new OneyConfirmRequest.Builder(redirectionPaymentRequest).build();
            return confirmPayment(confirmRequest);
        } catch (InvalidDataException e) {
            return e.toPaymentResponseFailure();
        }
    }

    @Override
    public PaymentResponse handleSessionExpired(TransactionStatusRequest transactionStatusRequest) {
        try {
            OneyTransactionStatusRequest oneyTransactionStatusRequest = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                    .fromTransactionStatusRequest(transactionStatusRequest)
                    .build();
            StringResponse response = this.httpClient.initiateGetTransactionStatus(oneyTransactionStatusRequest);

            // check response
            if (response.getCode() != HTTP_OK) {
                return createFailureResponse(response);
            }

            // check response message
            else {
                TransactionStatusResponse transactionStatusResponse = TransactionStatusResponse.createTransactionStatusResponseFromJson(response.getContent(), oneyTransactionStatusRequest.getEncryptKey());
                if ("FAVORABLE".equals(transactionStatusResponse.getStatusPurchase().getStatusCode())) {
                    OneyConfirmRequest confirmRequest = new OneyConfirmRequest.Builder(transactionStatusRequest).build();
                    return confirmPayment(confirmRequest);
                } else {
                    return getPaymentStatus(oneyTransactionStatusRequest, 0);

                }

            }
        } catch (PluginTechnicalException e) {
            return e.toPaymentResponseFailure();
        }
    }

    private String addErrorCode(TransactionStatusResponse response) {
        return ERROR_CODE + response.getStatusPurchase().getStatusCode();
    }

    private PaymentResponse getPaymentStatus(OneyTransactionStatusRequest oneyTransactionStatusRequest, int iteration) {
        try {
            StringResponse response = this.httpClient.initiateGetTransactionStatus(oneyTransactionStatusRequest);

            // check response
            if (response.getCode() != HTTP_OK) {
                // return an error response
                return createFailureResponse(response);
            }

            // check response message
            else {
                TransactionStatusResponse transactionStatusResponse = TransactionStatusResponse.createTransactionStatusResponseFromJson(response.getContent(), oneyTransactionStatusRequest.getEncryptKey());
                PurchaseStatus purchaseStatus = transactionStatusResponse.getStatusPurchase();
                String purchaseReference = oneyTransactionStatusRequest.getPurchaseReference();
                switch (purchaseStatus.getStatusCode()) {
                    case "PENDING":
                        // Payline: PENDING

                        // recursive ending condition
                        if (iteration >= MAX_ITERATION) {
                            return PaymentResponseOnHold.PaymentResponseOnHoldBuilder.aPaymentResponseOnHold()
                                    .withPartnerTransactionId(purchaseReference)
                                    .withOnHoldCause(OnHoldCause.SCORING_ASYNC)
                                    .build();
                        } else {
                            // wait for 1 second
                            long endTime = System.currentTimeMillis() + 1000;
                            while (System.currentTimeMillis() < endTime){
                                // do nothing
                            }
                            // recursive call
                            return getPaymentStatus(oneyTransactionStatusRequest, ++iteration);
                        }
                    case "REFUSED":
                        // Payline: REFUSED
                        return OneyErrorHandler.getPaymentResponseFailure(
                                FailureCause.REFUSED,
                                purchaseReference,
                                addErrorCode(transactionStatusResponse)
                        );
                    case "ABORTED":
                        // Payline:
                        // CANCEL or SESSION_EXPIRED according to Confluence mapping.
                        // Always CANCEL according to the former code...
                        return OneyErrorHandler.getPaymentResponseFailure(
                                FailureCause.CANCEL,
                                purchaseReference,
                                addErrorCode(transactionStatusResponse)
                        );
                    case "FAVORABLE":
                    case "FUNDED":
                    case "CANCELLED":
                    case "TO_BE_FUNDED":
                        // Payline: ACCEPTED
                        String message = purchaseStatus.getStatusLabel() != null ? purchaseStatus.getStatusLabel() : "OK";
                        return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                                .withStatusCode(Integer.toString(HTTP_OK))
                                .withTransactionDetails(new EmptyTransactionDetails())
                                .withPartnerTransactionId(purchaseReference)
                                .withMessage(new Message(Message.MessageType.SUCCESS, message))
                                .withTransactionAdditionalData(response.toString())
                                .build();
                    default:
                        // Should not be encountered !
                        LOGGER.error("Unexpected purchase status code encountered: " + purchaseStatus.getStatusCode());
                        return OneyErrorHandler.getPaymentResponseFailure(
                                FailureCause.PARTNER_UNKNOWN_ERROR,
                                purchaseReference,
                                "Unexpected purchase status: " + purchaseStatus.getStatusCode()
                        );
                }

            }

        } catch (PluginTechnicalException e) {
            return e.toPaymentResponseFailure();
        }
    }


    private PaymentResponse confirmPayment(OneyConfirmRequest confirmRequest) {
        try {
            // call confirm request
            StringResponse response = httpClient.initiateConfirmationPayment(confirmRequest);

            // check response
            if (response.getCode() != HTTP_OK) {
                // return an error response
                return createFailureResponse(response);
            }

            // check response message
            else {
                TransactionStatusResponse responseDecrypted = createTransactionStatusResponseFromJson(response.getContent(), confirmRequest.getEncryptKey());
                if (responseDecrypted == null || responseDecrypted.getStatusPurchase() == null) {
                    LOGGER.error("Transaction status response or purchase status is null");
                    return OneyErrorHandler.getPaymentResponseFailure(
                            FailureCause.REFUSED,
                            confirmRequest.getPurchaseReference(),
                            ERROR_CODE + "null");
                } else {
                    // return a paymentResponse from the Oney response status
                    OneyTransactionStatusRequest oneyTransactionStatusRequest = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                            .withLanguageCode(confirmRequest.getLanguageCode())
                            .withMerchantGuid(confirmRequest.getMerchantGuid())
                            .withPspGuid(confirmRequest.getPspGuid())
                            .withPurchaseReference(confirmRequest.getPurchaseReference())
                            .withEncryptKey(confirmRequest.getEncryptKey())
                            .withCallParameters(confirmRequest.getCallParameters())
                            .build();
                    return getPaymentStatus(oneyTransactionStatusRequest, 0);
                }
            }

        } catch (PluginTechnicalException e) {
            return e.toPaymentResponseFailure();
        }
    }

    /**
     * Create a PaymentResponseFailure from a StringResponse
     *
     * @param response
     * @return
     */
    private PaymentResponse createFailureResponse(StringResponse response) {
        FailureCause cause;
        String code;
        try {
            OneyFailureResponse failureResponse = new OneyFailureResponse(response.getCode(), response.getMessage(), response.getContent(), paymentErrorResponseFromJson(response.getContent()));
            cause = handleOneyFailureResponse(failureResponse);
            code = failureResponse.toPaylineErrorCode();
        } catch (MalformedResponseException e) {
            cause = FailureCause.PARTNER_UNKNOWN_ERROR;
            code = "unknown";
        }

        return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                    .withFailureCause(cause)
                    .withErrorCode(code)
                    .build();

    }
}
