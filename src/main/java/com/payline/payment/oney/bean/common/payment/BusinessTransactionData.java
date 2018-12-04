package com.payline.payment.oney.bean.common.payment;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import static com.payline.payment.oney.utils.OneyConstants.BUSINESS_TRANSACTION_TYPE;

public class BusinessTransactionData extends OneyBean {

    //todo objet doit s'appeler business transaction

    private String code;
    private Integer version;
    @SerializedName("business_transaction_type")
    private String businessTransactionType; //do enum ?

    public String getCode() {
        return code;
    }

    public Integer getVersion() {
        return version;
    }

    public String getBusinessTransactionType() {
        return businessTransactionType;
    }

    private BusinessTransactionData() {
    }

    public BusinessTransactionData(BusinessTransactionData.Builder builder) {
        this.code = builder.code;
        this.version = builder.version;
        this.businessTransactionType = builder.businessTransactionType;
    }

    public static class Builder {
        private String code;
        private Integer version;
        private String businessTransactionType; //do enum ?

        public static BusinessTransactionData.Builder aBusinessTransactionDataBuilder() {
            return new BusinessTransactionData.Builder();
        }


        public BusinessTransactionData.Builder withCode(String code) {
            this.code = code;
            return this;
        }

        public BusinessTransactionData.Builder withVersion(Integer version) {
            this.version = version;
            return this;
        }

        public BusinessTransactionData.Builder withBusinessTransactionType(String btt) {
            this.businessTransactionType = btt;
            return this;
        }

        public BusinessTransactionData.Builder verifyIntegrity() {
            if (this.code == null) {
                throw new IllegalStateException("BusinessTransactionData must have a code when built");
            } else return this;
        }

        public BusinessTransactionData.Builder fromPayline(ContractConfiguration contract){

            if (contract.getProperty(BUSINESS_TRANSACTION_TYPE) == null) {
                throw new IllegalStateException("Property "+ BUSINESS_TRANSACTION_TYPE+" doesn't exists");
            }

            this.code =  contract.getProperty(BUSINESS_TRANSACTION_TYPE).getValue();
            //Todo mapper version et business transactionType
            this.version = null;
            this.businessTransactionType = null;
            return this;
        }
        public BusinessTransactionData build(){
            return new BusinessTransactionData(this.verifyIntegrity());
        }
    }
}
