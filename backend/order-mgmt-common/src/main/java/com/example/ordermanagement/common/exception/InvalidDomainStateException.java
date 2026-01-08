package com.example.ordermanagement.common.exception;

public class InvalidDomainStateException extends RuntimeException {
    public InvalidDomainStateException(String message) {
        super(message);
    }
    
    public InvalidDomainStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
