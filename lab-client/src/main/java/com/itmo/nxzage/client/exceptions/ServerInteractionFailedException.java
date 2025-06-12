package com.itmo.nxzage.client.exceptions;

public class ServerInteractionFailedException extends RuntimeException {
    public ServerInteractionFailedException() {
        super();
    }

    public ServerInteractionFailedException(String message) {
        super(message);
    }

    public ServerInteractionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
