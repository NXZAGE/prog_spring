package com.itmo.nxzage.server.services.auth;

public interface HashService {
    public String getHash(String input);
    public String generateSault();
}
