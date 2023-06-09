package com.shopping.electronic.store.exception;

import lombok.Builder;

//custom exception that we are explicitly throwing in our program
@Builder
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource Not Found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
