package com.dabere.tirechange.app.models;

import lombok.Getter;

@Getter
public class BookingMessageResponse {
    private final String message;

    public BookingMessageResponse(String message) {
        this.message = message;
    }
}
