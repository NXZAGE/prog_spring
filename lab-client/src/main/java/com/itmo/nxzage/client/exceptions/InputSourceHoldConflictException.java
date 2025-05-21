package com.itmo.nxzage.client.exceptions;

public class InputSourceHoldConflictException extends RuntimeException {
    public InputSourceHoldConflictException() {
        super("Cannot switch input source: source is currently held.");
    }

    public InputSourceHoldConflictException(String message) {
        super(message);
    }
}
