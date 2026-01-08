package com.example.ordermanagement.modules.ordering.exception;

import com.example.ordermanagement.common.exception.InvalidDomainStateException;

public class InvalidOrderStateException extends InvalidDomainStateException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
    
    public InvalidOrderStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
