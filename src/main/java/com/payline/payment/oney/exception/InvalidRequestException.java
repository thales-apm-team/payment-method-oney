package com.payline.payment.oney.exception;

/**
 * Thrown when the incoming request is invalid or incomplete.
 */
public class InvalidRequestException extends Exception {

    public InvalidRequestException(String message) {
        super(message);
    }

}