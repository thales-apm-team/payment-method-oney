package com.payline.payment.oney.bean.common.payment;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.common.Amount;

import java.util.List;

public class PaymentData extends OneyBean {

    @SerializedName("payment_amount")
    private Float amount;
    @SerializedName("currency_code")
    private String currency; //a convertir ISO 4217
    @SerializedName("payment_type")
    private Integer paymentType; // enum de 0 à 2 ?
    @SerializedName("business_transaction_list")
    private List<BusinessTransactionData> businessTransactionList;


    public Float getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public List<BusinessTransactionData> getBusinessTransactionList() {
        return businessTransactionList;
    }

    private PaymentData() {
    }

    private PaymentData(PaymentData.Builder builder) {
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.paymentType = builder.paymentType;
        this.businessTransactionList = builder.businessTransactionList;
    }

    public static class Builder {
        private Float amount;
        private String currency;
        private Integer paymentType;
        private List<BusinessTransactionData> businessTransactionList;

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

        public PaymentData.Builder withBusinessTransactionList(List<BusinessTransactionData> businessTransactionList) {
            this.businessTransactionList = businessTransactionList;
            return this;
        }


        private PaymentData.Builder verifyIntegrity() {
            if (this.amount == null) {
                throw new IllegalStateException("PaymentData must have a amount when built");
            }
            if (this.currency == null) {
                throw new IllegalStateException("PaymentData must have a currency when built");
            } else {
                return this;
            }
        }

        public PaymentData build() {
            return new PaymentData(this.verifyIntegrity());
        }

        public PaymentData.Builder fromAmount(Amount amount) {
//todo mapper businessTransactionData et payment type (une constante ??)
            List<BusinessTransactionData> businessTransactionList = null;
            return PaymentData.Builder.aPaymentData()
                    .withAmount(amount.getAmountInSmallestUnit().floatValue())
                    .withCurrency(amount.getCurrency().getCurrencyCode())
                    //     .withBusinessTransactionList(businessTransactionList)
//                    .withPaymentType(PAYMENT_TYPE)
                    ;

        }


    }


}
