package com.payline.payment.oney.integration;

import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.PaymentFormContext;
import com.payline.pmapi.integration.AbstractPaymentIntegration;

import java.util.Map;

public class TestIt extends AbstractPaymentIntegration {
    @Override
    protected Map<String, ContractProperty> generateParameterContract() {
        return null;
    }

    @Override
    protected PaymentFormContext generatePaymentFormContext() {
        return null;
    }

    @Override
    protected String payOnPartnerWebsite(String s) {
        return null;
    }

    @Override
    protected String cancelOnPartnerWebsite(String s) {
        return null;
    }
}
