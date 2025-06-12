package com.itmo.nxzage.server.exceptions;

public class CommandRecognitionException extends RuntimeException {
    public CommandRecognitionException() {
        super();
    }
     
    public CommandRecognitionException(String message) {
        super(message);
    }

    public CommandRecognitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
