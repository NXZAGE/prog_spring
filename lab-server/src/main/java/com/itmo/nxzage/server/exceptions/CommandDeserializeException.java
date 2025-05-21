package com.itmo.nxzage.server.exceptions;

public class CommandDeserializeException extends Exception {
    public CommandDeserializeException() {
        super("Unable to parse command");
    }

    public CommandDeserializeException(String message) {
        super(message);
    }
    
    public CommandDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
