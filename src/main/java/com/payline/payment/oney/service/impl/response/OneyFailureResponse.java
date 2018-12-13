package com.payline.payment.oney.service.impl.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.common.FailureCause;

import java.util.List;

public class OneyFailureResponse extends OneyBean {


    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;
    @SerializedName("content")
    private String content;
    //todo implementContent to make mapping with Payline error
    @SerializedName("Payment_Error_Response")
    private PaymentErrorResponse paymentErrorContent;



    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

    public PaymentErrorResponse getPaymentErrorContent() {
        return paymentErrorContent;
    }

    public OneyFailureResponse() {
    }



    public static OneyFailureResponse createOneyFailureResponse(String json) {
        Gson parser = new Gson();
        return parser.fromJson(json, OneyFailureResponse.class);
    }


}


