package com.payline.payment.oney.bean.common;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class OneyError extends OneyBean{

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


    public static OneyError oneyErrorResponseFromJson (String json) {
        Gson parser = new Gson();
        return parser.fromJson(json, OneyError.class);
    }
}
