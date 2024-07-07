package com.dabere.tirechange.app.exceptions;

public class UnsupportedHttpResponseFormat extends RuntimeException{
    public UnsupportedHttpResponseFormat(String errorMessage) {
        super(errorMessage);
    }
}
