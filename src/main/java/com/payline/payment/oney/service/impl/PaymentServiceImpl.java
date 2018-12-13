package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.InvalidRequestException;
import com.payline.payment.oney.service.impl.request.OneyPaymentRequest;
import com.payline.payment.oney.service.impl.response.OneyFailureResponse;
import com.payline.payment.oney.service.impl.response.OneySuccessPaymentResponse;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.RequestContext;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseRedirect;
import com.payline.pmapi.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.payline.payment.oney.service.impl.response.OneyFailureResponse.createOneyFailureResponse;
import static com.payline.payment.oney.service.impl.response.OneySuccessPaymentResponse.paymentSuccessResponseFromJson;
import static com.payline.payment.oney.utils.OneyConstants.HTTP_OK;
import static com.payline.payment.oney.utils.OneyErrorHandler.getPaymentResponseFailure;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;
import static com.payline.payment.oney.utils.PluginUtils.generateReference;

public class PaymentServiceImpl implements PaymentService {

    private OneyHttpClient httpClient;
    private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);


    public PaymentServiceImpl() {
        this.httpClient = OneyHttpClient.getInstance();
    }

    @Override
    public PaymentResponse paymentRequest(PaymentRequest paymentRequest) {
        try {
            OneyPaymentRequest oneyRequest = OneyPaymentRequest.Builder.aOneyPaymentRequest()
                    .fromPaylineRequest(paymentRequest)
                    .build();
            Boolean isSandbox = paymentRequest.getEnvironment().isSandbox();


            StringResponse oneyResponse = httpClient.initiatePayment(oneyRequest, isSandbox);

            if (oneyResponse == null) {
                LOGGER.debug("InitiateSignatureResponse StringResponse is null !");
                LOGGER.error("Payment is null");
                return getPaymentResponseFailure(FailureCause.INTERNAL_ERROR);

            }
            //Cas ou une erreur est renvoyée au moment du paiement
            if (oneyResponse.getCode() != HTTP_OK) {
                //todo recupere error label
                //TODO FAIRE LE MAPPING DES ERREURS Oney-Payline
                OneyFailureResponse failureResponse = createOneyFailureResponse(oneyResponse.toString());
                LOGGER.error("Payment failed {} ", failureResponse.getContent());

                return PaymentResponseFailure.PaymentResponseFailureBuilder.aPaymentResponseFailure()
                        .withFailureCause(handleOneyFailureResponse(failureResponse))
                        .withErrorCode(failureResponse.getCode().toString())
                        .build();
            } else {
                //Response OK on recupere url envoyee par Oney
                OneySuccessPaymentResponse successResponse = paymentSuccessResponseFromJson(oneyResponse.getContent());

                URL redirectURL = new URL(successResponse.getReturnedUrl());
                PaymentResponseRedirect.RedirectionRequest.RedirectionRequestBuilder responseRedirectURL = PaymentResponseRedirect.RedirectionRequest.RedirectionRequestBuilder.aRedirectionRequest()
                        .withUrl(redirectURL);

                PaymentResponseRedirect.RedirectionRequest redirectionRequest = new PaymentResponseRedirect.RedirectionRequest(responseRedirectURL);
                Map<String, String> oneyContext = new HashMap<>();
                //RequestData
                oneyContext.put(OneyConstants.PSP_GUID_KEY, oneyRequest.getPspGuid());
                oneyContext.put(OneyConstants.MERCHANT_GUID_KEY, oneyRequest.getMerchantGuid());
                oneyContext.put(OneyConstants.EXTERNAL_REFERENCE_KEY, generateReference(oneyRequest));
                oneyContext.put(OneyConstants.PAYMENT_AMOUNT_KEY, oneyRequest.getPaymentData().getAmount().toString());
               //Ajout language code ??
                oneyContext.put(OneyConstants.LANGUAGE_CODE_KEY, paymentRequest.getLocale().getLanguage());

                RequestContext requestContext = RequestContext.RequestContextBuilder.aRequestContext()
                        .withRequestData(oneyContext)
                        .build();

                return PaymentResponseRedirect.PaymentResponseRedirectBuilder.aPaymentResponseRedirect()
                        .withRedirectionRequest(redirectionRequest)
                        .withPartnerTransactionId(oneyRequest.getMerchantRequestId())
                        .withStatusCode(String.valueOf(oneyResponse.getCode()))
                        .withRequestContext(requestContext)
                        .build();
            }

        } catch (IOException | URISyntaxException | InvalidRequestException e) {
            LOGGER.error("unable init the payment: {}", e.getMessage(), e);
            return getPaymentResponseFailure(FailureCause.INTERNAL_ERROR);
        }

    }

}
