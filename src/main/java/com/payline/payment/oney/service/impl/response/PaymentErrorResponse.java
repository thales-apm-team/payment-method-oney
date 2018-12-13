package com.payline.payment.oney.service.impl.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;

import java.util.List;

public class PaymentErrorResponse extends OneyBean {

    @SerializedName("error_list")
    private List<OneyError> errorList;

    public List<OneyError> getErrorList() {
        return errorList;
    }

    public class OneyError {
        @SerializedName("field")
        private String field;
        @SerializedName("error_code")
        private String errorCode;
        @SerializedName("error_label")
        private String errorLabel;


        public String getField() {
            return field;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getErrorLabel() {
            return errorLabel;
        }
    }

    public static PaymentErrorResponse paymentErrorResponseFromJson (String json) {
        Gson parser = new Gson();
        return parser.fromJson(json, PaymentErrorResponse.class);
    }

}


