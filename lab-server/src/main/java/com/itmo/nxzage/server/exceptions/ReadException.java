package com.itmo.nxzage.server.exceptions;

/**
 * Ошибка чтения потенциально возникающая при использовании {@link com.itmo.nxzage.server.DumpManager}
 */
public class ReadException extends RuntimeException {
    public ReadException() {
        super("Faild to read from file");
    }

    public ReadException(String message) {
        super(message);
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
