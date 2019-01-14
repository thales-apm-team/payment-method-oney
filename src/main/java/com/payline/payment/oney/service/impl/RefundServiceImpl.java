package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.request.OneyRefundRequest;
import com.payline.payment.oney.bean.response.OneyFailureResponse;
import com.payline.payment.oney.bean.response.TransactionStatusResponse;
import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.utils.OneyErrorHandler;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseSuccess;
import com.payline.pmapi.service.RefundService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

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
        //creation d'une OneyRefundRequest
        OneyRefundRequest oneyRefundRequest = OneyRefundRequest.Builder.aOneyRefundRequest()
                .fromRefundRequest(refundRequest)
                .build();
        boolean isSandbox = refundRequest.getEnvironment().isSandbox();

        try {
            StringResponse oneyResponse = httpClient.initiateRefundPayment(oneyRefundRequest, isSandbox);
            //handle Response
            if (oneyResponse == null) {
                LOGGER.debug("oneyResponse StringResponse is null !");
                LOGGER.error("Refund is null");
                return OneyErrorHandler.geRefundResponseFailure(FailureCause.INTERNAL_ERROR, oneyRefundRequest.getPurchaseReference());
            }
            //si erreur dans la requete http
            if (oneyResponse.getCode() != HTTP_OK) {
                OneyFailureResponse failureResponse = new OneyFailureResponse(oneyResponse.getCode(), oneyResponse.getMessage(), oneyResponse.getContent(), paymentErrorResponseFromJson(oneyResponse.getContent()));
                LOGGER.error("Refund failed {} ", failureResponse.getContent());

                return RefundResponseFailure.RefundResponseFailureBuilder.aRefundResponseFailure()
                        .withFailureCause(handleOneyFailureResponse(failureResponse))
                        .withErrorCode(failureResponse.getCode().toString())
                        .build();
            } else {
                //On dechiffre la response
                TransactionStatusResponse responseDecrypted = createTransactionStatusResponseFromJson(oneyResponse.getContent(), oneyRefundRequest.getEncryptKey());
                //Si Oney renvoie une message vide, on renvoi un Payment Failure response
                if (responseDecrypted.getStatusPurchase() == null) {
                    LOGGER.debug("oneyResponse StringResponse is null !");
                    LOGGER.error("Refund is null");
                    return OneyErrorHandler.geRefundResponseFailure(FailureCause.REFUSED, oneyRefundRequest.getPurchaseReference());
                }

                LOGGER.info("Payment has been cancelled");
                return RefundResponseSuccess.RefundResponseSuccessBuilder.aRefundResponseSuccess()
                        .withPartnerTransactionId(oneyRefundRequest.getPurchaseReference())
                        .withStatusCode(String.valueOf(oneyResponse.getCode()))
                        .withStatusCode(responseDecrypted.getStatusPurchase().getStatusCode())
                        .build();

            }


        } catch (IOException | URISyntaxException | DecryptException e) {
            LOGGER.error("unable init the payment", e);
            return OneyErrorHandler.geRefundResponseFailure(FailureCause.INTERNAL_ERROR, oneyRefundRequest.getPurchaseReference());

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
}
