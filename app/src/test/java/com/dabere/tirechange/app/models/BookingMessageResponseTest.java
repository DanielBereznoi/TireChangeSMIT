package com.dabere.tirechange.app.models;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class BookingMessageResponseTest {
    @Test
    void testGetMessage() {
        BookingMessageResponse response = new BookingMessageResponse("test");
        assertEquals("test", response.getMessage());
    }
}
