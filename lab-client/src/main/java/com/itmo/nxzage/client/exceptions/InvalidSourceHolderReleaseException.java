package com.itmo.nxzage.client.exceptions;

public class InvalidSourceHolderReleaseException extends RuntimeException {
    public InvalidSourceHolderReleaseException() {
        super("Cannot release input source: holder is not on top of the stack.");
    }

    public InvalidSourceHolderReleaseException(String message) {
        super(message);
    }
}
