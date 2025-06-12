package com.itmo.nxzage.common.util.exceptions;

public class DatagramDeserializationException extends RuntimeException {
    public DatagramDeserializationException(String message) {
        super(message);
    }

    public DatagramDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatagramDeserializationException(Throwable cause) {
        super(cause);
    }
}
