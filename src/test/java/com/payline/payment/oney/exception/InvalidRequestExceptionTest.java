package com.payline.payment.oney.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidRequestExceptionTest {


    @Test
    public void test() {
        InvalidRequestException invalidRequestException = new InvalidRequestException("test");
        Assertions.assertEquals("test", invalidRequestException.getMessage());
    }
}