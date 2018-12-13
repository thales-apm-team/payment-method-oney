package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.bean.common.AdditionalData;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.payment.oney.service.impl.request.OneyPaymentRequest;
import com.payline.payment.oney.service.impl.response.OneyFailureResponse;
import com.payline.payment.oney.service.impl.response.OneyResponse;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.payment.oney.utils.i18n.I18nService;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.oney.service.impl.response.OneyFailureResponse.createOneyFailureResponse;
import static com.payline.payment.oney.utils.OneyConstants.HTTP_OK;
import static com.payline.payment.oney.utils.OneyErrorHandler.getPaymentResponseFailure;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;
import static com.payline.pmapi.bean.common.Message.MessageType.SUCCESS;

public class PaymentWithRedirectionServiceImpl implements PaymentWithRedirectionService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentWithRedirectionServiceImpl.class);
    private OneyHttpClient httpClient;
    private I18nService i18n = I18nService.getInstance();

    public PaymentWithRedirectionServiceImpl() {
        this.httpClient = OneyHttpClient.getInstance();
    }

    @Override
    public PaymentResponse finalizeRedirectionPayment(RedirectionPaymentRequest redirectionPaymentRequest) {

        OneyConfirmRequest confirmRequest = OneyConfirmRequest.Builder.aOneyConfirmRequest()
                .fromPaylineRedirectionPaymentRequest(redirectionPaymentRequest)
                .build();
        boolean isSandbox = redirectionPaymentRequest.getEnvironment().isSandbox();
        try {
            PaymentResponse response =  validatePayment(confirmRequest, isSandbox);

            if (PaymentResponseSuccess.class.equals(response.getClass())) {
                return response;
            }
            else {
                // second try
                return validatePayment(confirmRequest, isSandbox);
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("unable to confirm the payment: {}", e.getMessage(), e);
          return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                  .withFailureCause(FailureCause.COMMUNICATION_ERROR)
                  .withErrorCode("503")
                  .build();
        }

    }

    @Override
    public PaymentResponse handleSessionExpired(TransactionStatusRequest transactionStatusRequest) {
        return null;
    }

    /**
     * Effectue l'appel http permettant de confirmer une commande
     *
     * @return
     */
    public PaymentResponse validatePayment(OneyConfirmRequest confirmRequest, boolean isSandbox) throws IOException, URISyntaxException {

        StringResponse oneyResponse = httpClient.initiateConfirmationPayment(confirmRequest, isSandbox);
        //todo tester reponse

        if (oneyResponse == null) {
            LOGGER.debug("InitiateSignatureResponse StringResponse is null !");
            LOGGER.error("Payment is null");
            return getPaymentResponseFailure(FailureCause.INTERNAL_ERROR);
        }

        if (oneyResponse.getCode() != HTTP_OK) {
            OneyFailureResponse failureResponse = createOneyFailureResponse(oneyResponse.toString());
            LOGGER.error("Payment failed {} ", failureResponse.getContent());

            return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                    .withFailureCause(handleOneyFailureResponse(failureResponse))
                    .withErrorCode(failureResponse.getCode().toString())
                    .build();

        }

        //Confirmation OK, on traite la reponse
        else {
            AdditionalData additionalData = AdditionalData.fromJson(oneyResponse.getContent());

            return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                    .withTransactionAdditionalData(additionalData.toJson())
                    .withPartnerTransactionId(confirmRequest.getPurchaseReference())
                    .withStatusCode(String.valueOf(oneyResponse.getCode()))
                    .withMessage(new Message(SUCCESS,oneyResponse.getMessage()))
                    .build();
        }
    }
}
