package com.payline.payment.oney.bean.common;

import com.google.gson.Gson;

public abstract class OneyBean {

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
