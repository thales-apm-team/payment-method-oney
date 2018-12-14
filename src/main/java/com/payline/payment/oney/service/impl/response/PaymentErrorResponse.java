package com.payline.payment.oney.service.impl.response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.payment.oney.bean.common.OneyError;

import java.lang.reflect.Type;
import java.util.List;

public class PaymentErrorResponse extends OneyBean {

    @SerializedName("error_list")
    private List<OneyError> errorList;

    public List<OneyError> getErrorList() {
        return errorList;
    }



    public PaymentErrorResponse(List<OneyError> oneyErrors){
        this.errorList = oneyErrors;
    }

    public static PaymentErrorResponse paymentErrorResponseFromJson(String json){
        //Specifier le type renvoye
        Type errorListType = new TypeToken<List<OneyError>>() {}.getType();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return  new PaymentErrorResponse (gson.fromJson(((JsonObject)jsonObject.get("Payments_Error_Response")).get("error_list"), errorListType));
    }




}


