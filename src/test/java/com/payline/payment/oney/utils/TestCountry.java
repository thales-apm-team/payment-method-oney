package com.payline.payment.oney.utils;

public enum TestCountry {

    BE("+32"), IT("+39"), FR("+33"), ES("+34");

    private String prefix;

    TestCountry(String prefix) {
        this.prefix = prefix;
    }

    public String getIndicatifTel() {
        return prefix;
    }
}
