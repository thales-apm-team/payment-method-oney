package com.payline.payment.oney.bean.common.payment;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.common.Amount;


public class PaymentData extends OneyBean {

    @SerializedName("payment_amount")
    private Float amount;
    @SerializedName("currency_code")
    private String currency; //a convertir ISO 4217
    @SerializedName("payment_type")
    private Integer paymentType; // enum de 0 à 2 ?
    @SerializedName("business_transaction")
    private BusinessTransactionData businessTransaction;


    public Float getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public BusinessTransactionData getBusinessTransaction() {
        return businessTransaction;
    }

    private PaymentData() {
    }

    private PaymentData(PaymentData.Builder builder) {
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.paymentType = builder.paymentType;
        this.businessTransaction = builder.businessTransaction;
    }

    public static class Builder {
        private Float amount;
        private String currency;
        private Integer paymentType;
        private BusinessTransactionData businessTransaction;

        public static PaymentData.Builder aPaymentData() {
            return new PaymentData.Builder();
        }

        public PaymentData.Builder withAmount(float amount) {
            this.amount = amount;
            return this;
        }

        public PaymentData.Builder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public PaymentData.Builder withPaymentType(int paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public PaymentData.Builder withBusinessTransactionList(BusinessTransactionData businessTransaction) {
            this.businessTransaction = businessTransaction;
            return this;
        }


        private PaymentData.Builder verifyIntegrity() {
            if (this.amount == null) {
                throw new IllegalStateException("PaymentData must have a amount when built");
            }
            if (this.currency == null) {
                throw new IllegalStateException("PaymentData must have a currency when built");
            }

            if (this.businessTransaction == null && (this.paymentType == null ||this.paymentType != 2) ){
                throw new IllegalStateException("PaymentData must have a businessTransaction when built");

            }else {
                return this;
            }
        }

        public PaymentData build() {
            return new PaymentData(this.verifyIntegrity());
        }

        public PaymentData.Builder fromAmount(Amount amount) {
//todo mapper businessTransactionData et payment type (une constante ??)
            return PaymentData.Builder.aPaymentData()
                    .withAmount(amount.getAmountInSmallestUnit().floatValue())
                    .withCurrency(amount.getCurrency().getCurrencyCode())
                    .withPaymentType(2)
                    //     .withBusinessTransactionList(businessTransactionList)
//                    .withPaymentType(PAYMENT_TYPE)
                    ;

        }


    }


}
