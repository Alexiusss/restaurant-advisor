package com.example.restaurant_advisor.error;

public class NotFoundException extends IllegalRequestDataException  {
    public NotFoundException(String msg) {
        super(msg);
    }
}