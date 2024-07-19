package com.dabere.tirechange.app.models;

import lombok.Getter;

@Getter
public class BookingMessageResponse {
    public final String message;

    public BookingMessageResponse(String message) {
        this.message = message;
    }
}
