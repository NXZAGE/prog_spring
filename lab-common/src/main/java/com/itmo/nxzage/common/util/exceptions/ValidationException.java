package com.itmo.nxzage.common.util.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException() {
        super("Validation failed");
    }

    public ValidationException(String message) {
        super("Validation failed: " + message);
    }
}
