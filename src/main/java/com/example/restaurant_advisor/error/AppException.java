package com.example.restaurant_advisor.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor()
@Getter
@ToString(callSuper = true)
public class AppException extends RuntimeException {
    private final ErrorType type;
    private final String msgCode;
}