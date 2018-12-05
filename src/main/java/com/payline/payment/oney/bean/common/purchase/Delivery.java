package com.payline.payment.oney.bean.common.purchase;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyBean;

public class Delivery extends OneyBean {

    @SerializedName("delivery_date")
    private String deliveryDate;
    @SerializedName("delivery_mode_code")
    private Integer deliveryModeCode; //creer enum ?
    @SerializedName("delivery_option")
    private Integer deliveryOption;
    @SerializedName("priority_delivery_code")
    private Integer priorityDeliveryCode;
    @SerializedName("address_type")
    private Integer addressType; //Creer ENUM ??
    private Recipient recipient; //Creer ENUM ??

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public Integer getDeliveryModeCode() {
        return deliveryModeCode;
    }

    public Integer getDeliveryOption() {
        return deliveryOption;
    }

    public Integer getPriorityDeliveryCode() {
        return priorityDeliveryCode;
    }

    public Integer getAddressType() {
        return addressType;
    }
    public Recipient getRecipient() {
        return recipient;
    }

    public Delivery() {
    }

    private Delivery(Delivery.Builder builder) {
        this.deliveryDate = builder.deliveryDate;
        this.deliveryModeCode = builder.deliveryModeCode;
        this.deliveryOption = builder.deliveryOption;
        this.priorityDeliveryCode = builder.priorityDeliveryCode;
        this.addressType = builder.addressType;
        this.recipient = builder.recipient;
    }

    public static class Builder {
        private String deliveryDate;
        private Integer deliveryModeCode; //creer enum ?
        private Integer deliveryOption;
        private Integer priorityDeliveryCode;
        private Integer addressType; //Creer ENUM ??
        private Recipient recipient; //Creer ENUM ??


        public static Delivery.Builder aDeliveryBuilder() {
            return new Delivery.Builder();
        }

        public Delivery.Builder withDeliveryDate(String date) {
            this.deliveryDate = date;
            return this;
        }
        public Delivery.Builder withDeliveryModeCode(Integer code) {
            this.deliveryModeCode = code;
            return this;
        }
        public Delivery.Builder withDeliveryOption(Integer option) {
            this.deliveryOption = option;
            return this;
        }
        public Delivery.Builder withPriorityDeliveryCode(Integer option) {
            this.priorityDeliveryCode = option;
            return this;
        }
        public Delivery.Builder withAddressType(Integer type) {
            this.addressType = type;
            return this;
        }

        public Delivery.Builder withRecipient(Recipient recipient) {
            this.recipient = recipient;
            return this;
        }

        private Delivery.Builder verifyIntegrity(){
            if (this.deliveryModeCode == null) {
                throw new IllegalStateException("Delivery must have a deliveryModeCode when built");
            }
            if (this.deliveryDate == null ||!this.deliveryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalStateException("Delivery must have a deliveryDate in format 'yyyy-MM-dd' when built");
            }
            if (this.deliveryOption == null) {
                throw new IllegalStateException("Delivery must have a deliveryOption when built");
            }
            if (this.addressType == null) {
                throw new IllegalStateException("Delivery must have a addressType when built");
            }

            if (this.addressType == 5 && this.recipient == null) {
                throw new IllegalStateException("Delivery must have a recipient when built");
            }
            return this;
        }

        public Delivery build(){
            return new Delivery(this.verifyIntegrity());
        }

    }
}
