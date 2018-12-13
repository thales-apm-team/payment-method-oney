package com.payline.payment.oney.response;

import com.payline.payment.oney.service.impl.response.OneyFailureResponse;
import com.payline.payment.oney.utils.http.StringResponse;
import com.payline.pmapi.bean.common.FailureCause;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponse;
import static com.payline.payment.oney.utils.OneyErrorHandler.handleOneyFailureResponseFromCause;
import static com.payline.payment.oney.utils.TestUtils.createStringResponse;

public class OneyFailureResponseTest {

    private FailureCause paylineFailureResponse;
    private OneyFailureResponse oneyFailureResponse;


    @Test
    public void testHandleOneyFailureResponse() {
        StringResponse stringResponse = createStringResponse(401, "Bad request", "{some content}");

        OneyFailureResponse failureCause = OneyFailureResponse.createOneyFailureResponse(stringResponse.toString());
        paylineFailureResponse = handleOneyFailureResponse(failureCause);
        Assert.assertEquals(FailureCause.REFUSED, paylineFailureResponse);

    }


    @Test
    public void tesHandleOneyFailureResponseFromCause() {
        StringResponse stringResponse = createStringResponse(400, "Bad request", "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"purchase.delivery.delivery_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"customer.customer_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"payment.payment_type\",\"error_code\":\"ERR_03\",\"error_label\":\"Format of the field is invalid [{Integer}]\"}]}}");

        OneyFailureResponse failureCause = OneyFailureResponse.createOneyFailureResponse(stringResponse.toString());

        paylineFailureResponse = handleOneyFailureResponseFromCause(failureCause);
        Assert.assertEquals(FailureCause.INVALID_FIELD_FORMAT, paylineFailureResponse);

    }


    @Test
    public void testJsonToMap() {
        Assert.fail();
        //todo find a way to get error_code value
        String json = "{\"Payments_Error_Response\":{\"error_list \":[{\"field\":\"purchase.delivery.delivery_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"purchase.item_list.category_code\",\"error_code\":\"ERR_04\",\"error_label\":\"Value of the field is invalid [{Integer}]\"},{\"field\":\"customer.customer_address.country_code\",\"error_code\":\"ERR_02\",\"error_label\":\"Size of the field should be equal to [3] characters\"},{\"field\":\"payment.payment_type\",\"error_code\":\"ERR_03\",\"error_label\":\"Format of the field is invalid [{Integer}]\"}]}}";

        System.out.println(json);
        //regex pour recuperer l'error code
//        Pattern p = Pattern.compile("error_code(.*?),");

        Pattern p = Pattern.compile("error_code(.*?),");
        Matcher m = p.matcher(json);
        String err ;
        System.out.println(m.group());



    }

}
