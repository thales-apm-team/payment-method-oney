package com.payline.payment.oney.service.impl.request;

/**
 * Pour lot2
 */

public class OneyRefundRequest extends OneyRequest {

    @SuppressWarnings(value = "unused")
    private String languageCode;
    @SuppressWarnings(value = "unused")
    private String merchantRequestId;
    @SuppressWarnings(value = "unused")
    private int reason; //or enum (0 = cancellation, 1 = fraud)
    @SuppressWarnings(value = "unused")
    private float amount; //must be present  if cancellationReason == 1  !! default value == total amount of payment
    @SuppressWarnings(value = "unused")
    private boolean refundFlag; // must be present  if cancellationReason == 1 true if is refunded else false

    //todo builder + method validate


}
