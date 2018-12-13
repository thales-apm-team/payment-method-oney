package com.payline.payment.oney.service.impl.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.payment.oney.bean.common.OneyError;

import java.util.List;

public class PaymentErrorResponse extends OneyBean {

    @SerializedName("error_list")
    private List<OneyError> errorList;

    public List<OneyError> getErrorList() {
        return errorList;
    }


    public static PaymentErrorResponse paymentErrorResponseFromJson (String json) {
        Gson parser = new Gson();
        return parser.fromJson(json, PaymentErrorResponse.class);
    }

}


