package com.itmo.nxzage.client.exceptions;

public class ResponseTimeOutException extends RuntimeException {
    public ResponseTimeOutException() {
        super();
    }

    public ResponseTimeOutException(String message) {
        super(message);
    }

    public ResponseTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
