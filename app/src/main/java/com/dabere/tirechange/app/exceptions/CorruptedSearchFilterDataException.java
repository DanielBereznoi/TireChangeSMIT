package com.dabere.tirechange.app.exceptions;

public class CorruptedSearchFilterDataException  extends RuntimeException
{
    public CorruptedSearchFilterDataException(String errorMessage) {
        super(errorMessage);
    }
}
