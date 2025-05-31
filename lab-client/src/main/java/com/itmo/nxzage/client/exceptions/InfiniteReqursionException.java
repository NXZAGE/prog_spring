package com.itmo.nxzage.client.exceptions;

public class InfiniteReqursionException extends RuntimeException {
    public InfiniteReqursionException() {
        super("Reqursion error: infinite reqursion");
    }
}
