package com.payline.payment.oney.bean.common.purchase;

import com.google.gson.annotations.SerializedName;
import com.payline.payment.oney.bean.common.OneyAddress;
import com.payline.payment.oney.bean.common.OneyBean;
import com.payline.payment.oney.bean.common.enums.AddressType;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.InvalidFieldFormatException;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.payment.oney.utils.PluginUtils;
import com.payline.payment.oney.utils.Required;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.payment.Order;
import com.payline.pmapi.bean.payment.request.PaymentRequest;

import java.text.SimpleDateFormat;

public class Delivery extends OneyBean {

    @Required
    @SerializedName("delivery_date")
    private String deliveryDate;

    @Required
    @SerializedName("delivery_mode_code")
    private Integer deliveryModeCode; //creer enum ?

    @Required
    @SerializedName("delivery_option")
    private Integer deliveryOption;

    @SerializedName("priority_delivery_code")
    private Integer priorityDeliveryCode;

    @Required
    @SerializedName("address_type")
    private Integer addressType; //Creer ENUM ??

    private Recipient recipient;

    @Required
    @SerializedName("delivery_address")
    private OneyAddress deliveryAddress;


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

    public OneyAddress getDeliveryAddress() {
        return deliveryAddress;
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
        this.deliveryAddress = builder.deliveryAddress;
    }

    public static class Builder {
        private String deliveryDate;
        private Integer deliveryModeCode; //creer enum ?
        private Integer deliveryOption;
        private Integer priorityDeliveryCode;
        private Integer addressType; //Creer ENUM ??
        private Recipient recipient; //Creer ENUM ??
        private OneyAddress deliveryAddress;


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

        public Delivery.Builder withDeliveryAddress(OneyAddress address) {
            this.deliveryAddress = address;
            return this;
        }

        private Delivery.Builder verifyIntegrity() throws InvalidDataException {

            if (this.deliveryModeCode == null) {
                throw new InvalidDataException("Delivery must have a deliveryModeCode when built", "Delivery.deliveryModeCode");
            }

            if (this.deliveryDate == null) {
                throw new InvalidDataException("Delivery must have a deliveryDate in format 'yyyy-MM-dd' when built", "Delivery.deliveryDate");
            } else if (!this.deliveryDate.matches(OneyConstants.DATE_FORMAT)) {
                throw new InvalidFieldFormatException("Delivery must have a deliveryDate in format 'yyyy-MM-dd' when built", "Delivery.deliveryDate");
            }

            if (this.deliveryOption == null) {
                throw new InvalidDataException("Delivery must have a deliveryOption when built", "Delivery.deliveryOption");
            }

            if (this.addressType == null) {
                throw new InvalidDataException("Delivery must have a addressType when built", "Delivery.addressType");
            }

            if (this.deliveryAddress == null) {
                throw new InvalidDataException("Delivery must have a deliveryAddress when built", "Delivery.deliveryAddress");
            }

            if (this.addressType == 5 && this.recipient == null) {
                throw new InvalidDataException("Delivery with addressType == 5 must have a recipient when built", "Delivery.recipient");
            }
            return this;
        }

        public Delivery.Builder fromPayline(PaymentRequest request) throws InvalidDataException {

            Order order = request.getOrder();
            if (order != null) {
                this.deliveryDate = (new SimpleDateFormat("yyyy-MM-dd")).format(order.getExpectedDeliveryDate());
                if (request.getOrder() != null) {
                    this.deliveryModeCode = PluginUtils.getOneyDeliveryModeCode(order.getDeliveryMode());
                    this.deliveryOption = PluginUtils.getOneyDeliveryOption(order.getDeliveryTime());
                }
            }

            AddressType addressTyp = AddressType.fromPaylineAddressType(Buyer.AddressType.DELIVERY);
            if (addressTyp != null) {
                this.addressType = addressTyp.getValue();
            }

            Buyer buyer = request.getBuyer();
            if (buyer != null) {
                this.recipient = Recipient.Builder.aRecipientBuilder().fromPayline(buyer).build();
                this.deliveryAddress = OneyAddress.Builder.aOneyAddressBuilder().fromPayline(buyer, Buyer.AddressType.DELIVERY)
                        .build();
            }
            return this;
        }

        public Delivery build() throws PluginTechnicalException {
            return new Delivery(this.verifyIntegrity());
        }

    }
}
