package com.neobis.mobiMarket.exception;

public class VerificationCodeNotFoundException extends RuntimeException{
    public VerificationCodeNotFoundException(String message) {
        super(message);
    }
}
