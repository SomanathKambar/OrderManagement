package com.example.ordermanagement.modules.delivery.exception;

import com.example.ordermanagement.common.exception.ResourceNotFoundException;

public class DeliveryPartnerNotFoundException extends ResourceNotFoundException {
    public DeliveryPartnerNotFoundException(String message) {
        super(message);
    }
    
    public DeliveryPartnerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
