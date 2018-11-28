package com.payline.payment.oney.service.impl;

import com.payline.payment.oney.utils.http.OneyHttpClient;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    private OneyHttpClient httpClient;



    public PaymentServiceImpl(){
        this.httpClient = OneyHttpClient.getInstance();
    }

    @Override
    public PaymentResponse paymentRequest(PaymentRequest paymentRequest) {
        //Todo
        return null;
    }
}
