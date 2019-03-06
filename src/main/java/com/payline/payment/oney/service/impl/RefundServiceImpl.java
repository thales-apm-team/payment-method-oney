package com.payline.payment.oney.service.impl;

import com.google.gson.JsonSyntaxException;
import com.payline.payment.oney.bean.common.OneyError;
import com.payline.payment.oney.bean.request.OneyRefundRequest;
import com.payline.payment.oney.bean.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.bean.response.OneyFailureResponse;
import com.payline.payment.oney.bean.response.TransactionStatusResponse;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.OneyErrorHandler;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseSuccess;
import com.payline.pmapi.logger.LogManager;
import com.payline.pmapi.service.RefundService;
import org.apache.logging.log4j.Logger;

import static com.payline.payment.oney.bean.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.bean.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
import static com.payline.payment.oney.utils.OneyConstants.HTTP_OK;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;

public class RefundServiceImpl implements RefundService {

    private OneyHttpClient httpClient;
    private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);

    public RefundServiceImpl() {
        this.httpClient = OneyHttpClient.getInstance();
    }

    @Override
    public RefundResponse refundRequest(RefundRequest refundRequest) {

        OneyRefundRequest oneyRefundRequest = null;
        try {
            //obtenir statut de la requete
            String status = handleStatusRequest(refundRequest);
            //faire une  transactionStatusRequest
            boolean refundFlag = getRefundFlag(status);

            //creation d'une OneyRefundRequest
            oneyRefundRequest = OneyRefundRequest.Builder.aOneyRefundRequest()
                    .fromRefundRequest(refundRequest, refundFlag)
                    .build();

            StringResponse oneyResponse = httpClient.initiateRefundPayment(oneyRefundRequest);
            //handle Response
            if (oneyResponse == null) {
                LOGGER.debug("oneyResponse StringResponse is null !");
                LOGGER.error("Refund is null");
                return OneyErrorHandler.geRefundResponseFailure(
                        FailureCause.PARTNER_UNKNOWN_ERROR,
                        oneyRefundRequest.getPurchaseReference(),
                        "Empty partner response");
            }
            //si erreur dans la requete http
            if (oneyResponse.getCode() != HTTP_OK) {
                OneyFailureResponse failureResponse = new OneyFailureResponse(oneyResponse.getCode(), oneyResponse.getMessage(), oneyResponse.getContent(), paymentErrorResponseFromJson(oneyResponse.getContent()));
                LOGGER.error("Refund failed {} ", failureResponse.getContent());

                return RefundResponseFailure.RefundResponseFailureBuilder.aRefundResponseFailure()
                        .withFailureCause(handleOneyFailureResponse(failureResponse))
                        .withErrorCode(failureResponse.toPaylineErrorCode())
                        .build();
            } else {
                //On dechiffre la response
                TransactionStatusResponse responseDecrypted = createTransactionStatusResponseFromJson(oneyResponse.getContent(), oneyRefundRequest.getEncryptKey());

                //Si Oney renvoie une message vide, on renvoi un Payment Failure response
                if (responseDecrypted.getStatusPurchase() == null) {
                    LOGGER.debug("oneyResponse StringResponse is null !");
                    LOGGER.error("Refund is null");
                    return OneyErrorHandler.geRefundResponseFailure(
                            FailureCause.REFUSED,
                            oneyRefundRequest.getPurchaseReference(),
                            "Purchase status : null");
                }

                LOGGER.info("Payment has been cancelled");
                return RefundResponseSuccess.RefundResponseSuccessBuilder.aRefundResponseSuccess()
                        .withPartnerTransactionId(oneyRefundRequest.getPurchaseReference())
                        .withStatusCode(String.valueOf(oneyResponse.getCode()))
                        .withStatusCode(responseDecrypted.getStatusPurchase().getStatusCode())
                        .build();

            }

        } catch( JsonSyntaxException e ){
            LOGGER.error( "Unable to parse JSON content", e );
            String ref = oneyRefundRequest != null ? oneyRefundRequest.getPurchaseReference() : OneyConstants.EXTERNAL_REFERENCE_TYPE + "|" + refundRequest.getOrder().getReference();
            return OneyErrorHandler.geRefundResponseFailure(
                    FailureCause.COMMUNICATION_ERROR, ref, "Unable to parse JSON content"
            );
        }
        catch (InvalidDataException e) {
            LOGGER.error("unable init the payment", e);
            return e.toRefundResponseFailure();

        } catch (PluginTechnicalException e) {
            LOGGER.error("unable init the payment", e);
            String ref = oneyRefundRequest != null ? oneyRefundRequest.getPurchaseReference() : "null";
            return OneyErrorHandler.geRefundResponseFailure(
                    e.getFailureCause(),
                    ref,
                    e.getErrorCodeOrLabel());
        }

    }

    @Override
    public boolean canMultiple() {
        return true;
    }

    @Override
    public boolean canPartial() {
        return true;
    }

    /**
     * Obteniir le statut d'une transaction en cours
     *
     * @param refundRequest
     * @return
     */
    public String handleStatusRequest(RefundRequest refundRequest) throws PluginTechnicalException {
        OneyTransactionStatusRequest oneyTransactionStatusRequest = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .fromRefundRequest(refundRequest)
                .build();
        String transactionStatusCode = "";
        try {
            StringResponse status = this.httpClient.initiateGetTransactionStatus(oneyTransactionStatusRequest);
            //l'appel est OK on gere selon la response
            if (status.getCode() == HTTP_OK) {
                TransactionStatusResponse response = TransactionStatusResponse.createTransactionStatusResponseFromJson(status.getContent(), oneyTransactionStatusRequest.getEncryptKey());
                transactionStatusCode = response.getStatusPurchase() == null ? null : response.getStatusPurchase().getStatusCode();
            }

        } catch (PluginTechnicalException e) {
            LOGGER.error("unable to get transaction status", e);
            throw e;

        }
        return transactionStatusCode;

    }

    /**
     * Check request's status in order to determine if the paiement's status is compatible with a refund or a cancel request.
     *
     * @param transactionStatusRequest
     * @return refundFlag (for refund or cancel request)
     */
    protected boolean getRefundFlag(String transactionStatusRequest) {

        if (transactionStatusRequest != null) {

            switch (transactionStatusRequest) {
                case "FUNDED":
                    return true;

                case "PENDING":
                case "FAVORABLE":
                    return false;

                default:
                    break;
            }

        }

        // REFUSED / ABORTED / CANCELLED are not valid for refund or cancel ...
        LOGGER.error("Resquest's status {} is not valid for refund or cancel", transactionStatusRequest);
        return false;

    }
}
