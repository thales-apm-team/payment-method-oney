package com.payline.payment.oney.bean.common;

import com.google.gson.annotations.SerializedName;
import com.payline.pmapi.bean.Request;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

public class Customer extends OneyBean {
    @SerializedName("trust_flag")
    private Integer trustFlag;
    @SerializedName("customer_external_code")
    private String customerExternalCode;
    @SerializedName("language_code")
    private String languageCode;
   @SerializedName("identity")
    private CustomerIdentity identity;

    public Integer getTrustFlag() {
        return trustFlag;
    }

    public String getCustomerExternalCode() {
        return customerExternalCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public CustomerIdentity getIdentity() {
        return identity;
    }

    private Customer(){}

    public Customer(Customer.Builder builder) {
        this.trustFlag = builder.trustFlag;
        this.customerExternalCode = builder.customerExternalCode;
        this.languageCode = builder.languageCode;
        this.identity = builder.identity;
    }

    public static class Builder {
        private Integer trustFlag;
        private String customerExternalCode;
        private String languageCode;
        private CustomerIdentity identity;

        public static Customer.Builder aCustomBuilder(){
            return new Customer.Builder();
        }

        public Customer.Builder withTrustFlag(Integer flag){
            this.trustFlag = flag;
            return this;
        }
        public Customer.Builder withCustumerExternalCode(String code){
            this.customerExternalCode = code;
            return this;
        }
        public Customer.Builder withLanguageCode(String code){
            this.languageCode = code;
            return this;
        }
        public Customer.Builder withCustomerIdentity(CustomerIdentity id){
            this.identity = id;
            return this;
        }

       private Customer.Builder verifyIntegrity(){

            if (this.customerExternalCode == null) {
                throw new IllegalStateException("Customer must have a customerExternalCode when built");
            }
            if (this.languageCode == null) {
                throw new IllegalStateException("Customer must have a languageCode when built");
            }
            if (this.identity == null) {
                throw new IllegalStateException("Customer must have a identity when built");
            }
            else return this;
        }

        public Customer.Builder fromPaylineRequest(PaymentRequest request){
            this.trustFlag = null;
            this.customerExternalCode = request.getBuyer().getCustomerIdentifier();
            this.languageCode = request.getLocale().getLanguage();
            this.identity =CustomerIdentity.Builder.aCustomerIdentity()
            .fromPayline(request.getBuyer())
            .build();

            return this;
        }

        public Customer build(){
            return new Customer(this.verifyIntegrity());
        }
    }
}
