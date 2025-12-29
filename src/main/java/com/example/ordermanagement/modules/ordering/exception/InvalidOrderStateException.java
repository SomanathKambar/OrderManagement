package com.example.ordermanagement.modules.ordering.exception;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
    
    public InvalidOrderStateException(String message, Throwable cause) {
        super(message, cause);
    }
}