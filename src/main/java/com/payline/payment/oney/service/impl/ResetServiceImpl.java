package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.request.OneyRefundRequest;
import com.payline.payment.oney.bean.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.bean.response.OneyFailureResponse;
import com.payline.payment.oney.bean.response.TransactionStatusResponse;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.reset.request.ResetRequest;
import com.payline.pmapi.bean.reset.response.ResetResponse;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseFailure;
import com.payline.pmapi.bean.reset.response.impl.ResetResponseSuccess;
import com.payline.pmapi.logger.LogManager;
import com.payline.pmapi.service.ResetService;
import org.apache.logging.log4j.Logger;

import static com.payline.payment.oney.bean.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.bean.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
import static com.payline.payment.oney.utils.OneyConstants.HTTP_OK;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;

public class ResetServiceImpl implements ResetService {

    private OneyHttpClient httpClient;
    private static final Logger LOGGER = LogManager.getLogger(ResetServiceImpl.class);

    public ResetServiceImpl() {
        this.httpClient = OneyHttpClient.getInstance();
        ;
    }

    @Override
    public ResetResponse resetRequest(ResetRequest resetRequest) {
        OneyRefundRequest oneyRefundRequest = null;
        try {
            //obtenir statut de la requete
            String status = handleStatusRequest(resetRequest);
            //faire une  transactionStatusRequest
            boolean refundFlag = PluginUtils.getRefundFlag(status);

            //creation d'une OneyRefundRequest
            oneyRefundRequest = OneyRefundRequest.Builder.aOneyRefundRequest()
                    .fromResetRequest(resetRequest, refundFlag)
                    .build();

            StringResponse oneyResponse = httpClient.initiateRefundPayment(oneyRefundRequest, resetRequest.getEnvironment().isSandbox());
            //handle Response
            if (oneyResponse == null) {
                LOGGER.debug("oneyResponse StringResponse is null !");
                LOGGER.error("Reset is null");


                return ResetResponseFailure.ResetResponseFailureBuilder.aResetResponseFailure()
                        .withPartnerTransactionId(resetRequest.getPartnerTransactionId())
                        .withFailureCause(FailureCause.PARTNER_UNKNOWN_ERROR)
                        .withErrorCode("Empty partner response")
                        .build();
            }
            //si erreur dans la requete http
            if (oneyResponse.getCode() != HTTP_OK) {
                OneyFailureResponse failureResponse = new OneyFailureResponse(oneyResponse.getCode(), oneyResponse.getMessage(), oneyResponse.getContent(), paymentErrorResponseFromJson(oneyResponse.getContent()));
                LOGGER.error("Reset failed {} ", failureResponse.getContent());

                return ResetResponseFailure.ResetResponseFailureBuilder.aResetResponseFailure()
                        .withFailureCause(handleOneyFailureResponse(failureResponse))
                        .withErrorCode(failureResponse.toPaylineErrorCode())
                        .build();
            } else {
                //On dechiffre la response
                TransactionStatusResponse responseDecrypted = createTransactionStatusResponseFromJson(oneyResponse.getContent(), oneyRefundRequest.getEncryptKey());

                //Si Oney renvoie une message vide, on renvoi un Payment Failure response
                if (responseDecrypted.getStatusPurchase() == null) {
                    LOGGER.debug("oneyResponse StringResponse is null !");
                    LOGGER.error("Reset is null");
                    return ResetResponseFailure.ResetResponseFailureBuilder.aResetResponseFailure()
                            .withPartnerTransactionId(resetRequest.getPartnerTransactionId())
                            .withErrorCode("Purchase status : null")
                            .withFailureCause(FailureCause.REFUSED)
                            .build();
                }

                LOGGER.info("Reset Success");
                return ResetResponseSuccess.ResetResponseSuccessBuilder.aResetResponseSuccess()
                        .withPartnerTransactionId(resetRequest.getPartnerTransactionId())
                        .withStatusCode(responseDecrypted.getStatusPurchase().getStatusCode())
                        .build();

            }

        } catch (PluginTechnicalException e) {
            LOGGER.error("unable init the reset", e);
            return e.toResetResponseFailure();
        }catch (RuntimeException e) {
            LOGGER.error("Unexpected plugin error", e);
            return ResetResponseFailure.ResetResponseFailureBuilder.aResetResponseFailure()
                    .withErrorCode(PluginTechnicalException.runtimeErrorCode(e))
                    .withFailureCause(FailureCause.INTERNAL_ERROR)
                    .build();
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
     * @param resetRequest
     * @return
     */
    public String handleStatusRequest(ResetRequest resetRequest) throws PluginTechnicalException {
        OneyTransactionStatusRequest oneyTransactionStatusRequest = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .fromResetRequest(resetRequest)
                .build();
        String transactionStatusCode = "";
        try {
            StringResponse status = this.httpClient.initiateGetTransactionStatus(oneyTransactionStatusRequest, resetRequest.getEnvironment().isSandbox());
            //l'appel est OK on gere selon la response
            if (status.getCode() == HTTP_OK) {
                TransactionStatusResponse response = createTransactionStatusResponseFromJson(status.getContent(), oneyTransactionStatusRequest.getEncryptKey());
                transactionStatusCode = response.getStatusPurchase() == null ? null : response.getStatusPurchase().getStatusCode();
            }

        } catch (PluginTechnicalException e) {
            LOGGER.error("unable to get transaction status", e);
            throw e;

        }
        return transactionStatusCode;

    }
}
