package com.itmo.nxzage.server.exceptions;

public class DumpException extends RuntimeException {
    public DumpException() {
        super("Fault occured while writing to the file");
    }

    public DumpException(String message) {
        super(message);
    }

    public DumpException(String message, Throwable cause) {
        super(message, cause);
    }
}
