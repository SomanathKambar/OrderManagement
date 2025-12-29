package com.example.ordermanagement.modules.ordering.exception;

import com.example.ordermanagement.common.exception.ResourceNotFoundException;

public class OrderNotFoundException extends ResourceNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
    
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
