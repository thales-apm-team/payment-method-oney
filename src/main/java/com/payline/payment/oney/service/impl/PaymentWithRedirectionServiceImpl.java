package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.service.impl.request.OneyConfirmRequest;
import com.payline.payment.oney.service.impl.request.OneyTransactionStatusRequest;
import com.payline.payment.oney.service.impl.response.OneyFailureResponse;
import com.payline.payment.oney.service.impl.response.TransactionStatusResponse;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.payment.oney.utils.i18n.I18nService;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.common.Message;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.impl.EmptyTransactionDetails;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;
import com.payline.pmapi.service.PaymentWithRedirectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.payline.payment.oney.service.impl.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.service.impl.response.TransactionStatusResponse.createTransactionStatusResponseFromJson;
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
            PaymentResponse response = validatePayment(confirmRequest, isSandbox);

            if (PaymentResponseSuccess.class.equals(response.getClass())) {
                return response;
            } else {
                // second try
                return validatePayment(confirmRequest, isSandbox);
            }
        } catch (IOException | URISyntaxException | DecryptException e) {
            LOGGER.error("unable to confirm the payment: {}", e.getMessage(), e);
            return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                    .withFailureCause(FailureCause.COMMUNICATION_ERROR)
                    .withErrorCode("503")
                    .build();
        }

    }

    @Override
    public PaymentResponse handleSessionExpired(TransactionStatusRequest transactionStatusRequest) {

        OneyTransactionStatusRequest oneyTransactionStatusRequest = OneyTransactionStatusRequest.Builder.aOneyGetStatusRequest()
                .fromTransactionStatusRequest(transactionStatusRequest)
                .build();
        try {
            //retrouver les donnees de paiement
            boolean isSandbox = transactionStatusRequest.getEnvironment().isSandbox();
            StringResponse status = this.httpClient.initiateGetTransactionStatus(oneyTransactionStatusRequest, isSandbox);
            if (status.getCode() == HTTP_OK) {
                TransactionStatusResponse response = createTransactionStatusResponseFromJson(status.getContent());
                //gerer les cas PENDING, FAVORABLE,
                switch (response.getStatusPurchase().getStatusCode()) {
                    case "PENDING":
                        //renvoi  à l'utilisateur ??
                        break;
                    case "ABORTED":
                        //demande annuléee

                        break;
                    case "REFUSED":
                        //demande rejetee

                        break;
                    case "FAVORABLE":
                        //renvoyer une paymentResponseSuccess avec donnees ?

                        break;
                    case "FUNDED":
                        //renvoyer une paymentResponseSuccess avec donnees
                        break;
                    case "CANCELLED":
                        break;

                }

            }


            //change value
            return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                    .withStatusCode("200")
                    .withMessage(new Message(Message.MessageType.SUCCESS, "OK"))
                    .build();
        } catch (IOException | DecryptException | URISyntaxException e) {
            LOGGER.error("unable to handle the session expiration: {}", e.getMessage(), e);
//    return OneyFailureResponse.fromJson();}
            return null;
        }
//        TransactionStatusResponse createTransactionStatusResponseFromJson


    }

    /**
     * Effectue l'appel http permettant de confirmer une commande
     *
     * @return
     */
    public PaymentResponse validatePayment(OneyConfirmRequest confirmRequest, boolean isSandbox) throws
            IOException, URISyntaxException, DecryptException {

        StringResponse oneyResponse = httpClient.initiateConfirmationPayment(confirmRequest, isSandbox);

        if (oneyResponse == null) {
            LOGGER.debug("InitiateSignatureResponse StringResponse is null !");
            LOGGER.error("Payment is null");
            return getPaymentResponseFailure(FailureCause.INTERNAL_ERROR);
        }

        if (oneyResponse.getCode() != HTTP_OK) {
            OneyFailureResponse failureResponse = new OneyFailureResponse(oneyResponse.getCode(), oneyResponse.getMessage(), oneyResponse.getContent(), paymentErrorResponseFromJson(oneyResponse.getContent()));

            LOGGER.error("Payment failed {} ", failureResponse.getContent());

            return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                    .withFailureCause(handleOneyFailureResponse(failureResponse))
                    .withErrorCode(failureResponse.getCode().toString())
                    .build();
        }
        //Confirmation OK, on traite la reponse
        else {
//todo Dechiffrer la reponse
            TransactionStatusResponse responseDecrypted = createTransactionStatusResponseFromJson(oneyResponse.getContent());

//            AdditionalData additionalData = AdditionalData.fromJson(responseDecrypted.toString());
            //Additional data  : ajouter purchaseReference ? amount ? transaction status ??

            return PaymentResponseSuccess.PaymentResponseSuccessBuilder.aPaymentResponseSuccess()
                    .withTransactionAdditionalData(responseDecrypted.toString())
                    .withPartnerTransactionId(confirmRequest.getPurchaseReference())
                    .withStatusCode(String.valueOf(oneyResponse.getCode()))
                    .withMessage(new Message(SUCCESS, responseDecrypted.getStatusPurchase().getStatusLabel()))
                    .withTransactionDetails(new EmptyTransactionDetails())
                    .build();
        }
    }
}
