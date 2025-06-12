package com.itmo.nxzage.common.util.exceptions;

public class PacketSerializationException extends RuntimeException {
    public PacketSerializationException(String message) {
        super(message);
    }

    public PacketSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketSerializationException(Throwable cause) {
        super(cause);
    }
}

