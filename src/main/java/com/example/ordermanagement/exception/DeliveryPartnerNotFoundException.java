package com.example.ordermanagement.exception;

public class DeliveryPartnerNotFoundException extends RuntimeException {
    public DeliveryPartnerNotFoundException(String message) {
        super(message);
    }
    
    public DeliveryPartnerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}