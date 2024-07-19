package com.dabere.tirechange.app.exceptions;

public class UnsupportedHttpRequestOperator extends RuntimeException {
    public UnsupportedHttpRequestOperator(String errorMessage) {
        super(errorMessage);
    }
}
