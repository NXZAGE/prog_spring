package com.itmo.nxzage.server.services.auth;

public interface Accessable<T> {
    default public void checkAccess(T resource) throws IllegalAccessException {
        return;
    };
}
