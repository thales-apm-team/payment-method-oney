package com.payline.payment.oney.service.impl.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;

import static com.payline.payment.oney.service.impl.response.PaymentErrorResponse.paymentErrorResponseFromJson;

public class OneyFailureResponse extends OneyBean {


    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;
    @SerializedName("content")
    private String content;

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

    public OneyFailureResponse(int code, String message,String responseError) {
        this.code = code;
        this.message = message;
        this.paymentErrorContent = paymentErrorResponseFromJson(responseError);

    }

    public void setPaymentErrorContent(String jsonErrorResponse) {
        this.paymentErrorContent =paymentErrorResponseFromJson(jsonErrorResponse);
    }

    public static OneyFailureResponse fromJson(String json) {
        Gson parser = new Gson();
        return parser.fromJson(json, OneyFailureResponse.class);
    }






}


