package com.payline.payment.oney.service.impl.request;

public class OneyRefundRequest extends OneyRequest {


    private String languageCode;

    private String merchantRequestId;
    private int reason; //or enum (0 = cancellation, 1 = fraud)
    private float amount; //must be present  if cancellationReason == 1  !! default value == total amount of payment
    private boolean refundFlag; // must be present  if cancellationReason == 1 true if is refunded else false

    //todo builder + method validate


}
