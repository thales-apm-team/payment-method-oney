package com.payline.payment.oney.response;

import com.payline.payment.oney.service.impl.response.OneyFailureResponse;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.payline.payment.oney.service.impl.response.PaymentErrorResponse.paymentErrorResponseFromJson;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponseFromCause;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class OneyFailureResponseTest {

    private FailureCause paylineFailureResponse;
    private OneyFailureResponse oneyFailureResponse;


    @Test
    public void testHandleOneyFailureResponse() {
        StringResponse stringResponse = createStringResponse(401, "Bad request", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.fromJson(stringResponse.toString());
        paylineFailureResponse = handleOneyFailureResponse(failureCause);
        Assertions.assertEquals(FailureCause.REFUSED, paylineFailureResponse);

    }

    @Test
    public void testOneyFailureResponse() {
        StringResponse stringResponse = createStringResponse(400, "Bad request", "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"purchase.delivery.delivery_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"customer.customer_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"payment.payment_type\",\"error_code\":\"ERR_03\",\"error_label\":\"Format of the field is invalid [{Integer}]\"}]}}");

        OneyFailureResponse failureCause = new OneyFailureResponse(stringResponse.getCode(), stringResponse.getMessage(), stringResponse.getContent(), paymentErrorResponseFromJson(stringResponse.getContent()));

        Assertions.assertEquals("400", failureCause.getCode().toString());
        Assertions.assertEquals("Bad request", failureCause.getMessage());
        Assertions.assertNotNull(failureCause.getContent());
        Assertions.assertNotNull(failureCause.getPaymentErrorContent());

    }


    @Test
    public void tesHandleOneyFailureResponseFromCause() {
        String json = "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"payment.business_transaction.code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be less than or equal to [5] characters\"}]}}";

        StringResponse stringResponse = createStringResponse(400, "Bad request", json);

        OneyFailureResponse failureCause = new OneyFailureResponse(stringResponse.getCode(), stringResponse.getMessage(), stringResponse.getContent(), paymentErrorResponseFromJson(stringResponse.getContent()));

        paylineFailureResponse = handleOneyFailureResponseFromCause(failureCause);
        Assertions.assertEquals(FailureCause.INVALID_FIELD_FORMAT, paylineFailureResponse);

    }


}
