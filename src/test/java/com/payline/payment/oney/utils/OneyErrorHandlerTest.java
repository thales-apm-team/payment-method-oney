package com.payline.payment.oney.utils;

import com.payline.payment.oney.bean.response.OneyFailureResponse;
import com.payline.payment.oney.exception.MalformedResponseException;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.bean.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class OneyErrorHandlerTest {

    private String externalReference = "externalReference";

    @Test
    public void testHandleOneyFailureResponse401() {
        StringResponse stringResponse = createStringResponse(401, "Unauthorized", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.REFUSED, paylineFailureResponse);

    }

    @Test
    public void testHandleOneyFailureResponse403() {
        StringResponse stringResponse = createStringResponse(403, "Forbidden", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.REFUSED, paylineFailureResponse);

    }

    @Test
    public void testHandleOneyFailureResponse500() {
        StringResponse stringResponse = createStringResponse(500, "Internal Server Error", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.REFUSED, paylineFailureResponse);

    }

    @Test
    public void testHandleOneyFailureResponse404() {
        StringResponse stringResponse = createStringResponse(404, "Not Found", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.COMMUNICATION_ERROR, paylineFailureResponse);

    }

    @Test
    public void testHandleOneyFailureResponse408() {
        StringResponse stringResponse = createStringResponse(408, "Request Time-out", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.COMMUNICATION_ERROR, paylineFailureResponse);

    }

    @Test
    public void testHandleOneyFailureResponse429() {
        StringResponse stringResponse = createStringResponse(429, "Too Many Requests", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.COMMUNICATION_ERROR, paylineFailureResponse);

    }

    @Test
    public void testHandleOneyFailureResponse503() {
        StringResponse stringResponse = createStringResponse(503, "Service Unavailable", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.COMMUNICATION_ERROR, paylineFailureResponse);
    }

    @Test
    public void testHandleOneyFailureResponse504() {
        StringResponse stringResponse = createStringResponse(504, "Gateway Time-out", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.COMMUNICATION_ERROR, paylineFailureResponse);
    }

    @Test
    public void geRefundResponseFailure() {
        final FailureCause failureCause = FailureCause.SESSION_EXPIRED;
        RefundResponseFailure result = OneyErrorHandler.geRefundResponseFailure(failureCause, externalReference, "");

        Assertions.assertEquals(externalReference, result.getPartnerTransactionId());
        Assertions.assertEquals(failureCause, result.getFailureCause());
    }

    @Test
    public void handleOneyFailureResponseFromCause() throws MalformedResponseException {
        String json = "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"payment.business_transaction.code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be less than or equal to [5] characters\"}]}}";

        StringResponse stringResponse = createStringResponse(400, "Bad request", json);

        OneyFailureResponse failureCause = new OneyFailureResponse(stringResponse.getCode(), stringResponse.getMessage(), stringResponse.getContent(), paymentErrorResponseFromJson(stringResponse.getContent()));

        FailureCause paylineFailureResponse = OneyErrorHandler.handleOneyFailureResponseFromCause(failureCause);
        Assertions.assertEquals(FailureCause.INVALID_FIELD_FORMAT, paylineFailureResponse);

    }

}
