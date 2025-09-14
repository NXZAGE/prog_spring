package com.itmo.nxzage.server.services.auth;

import java.security.SecureRandom;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class SHA512 implements HashService {
    private static int SALT_LENGTH = 32;

    @Override
    public String generateSault() {
        return generateSaltHex(SALT_LENGTH);
    }

    @Override
    public String getHash(String input) {
        return DigestUtils.sha512Hex(input);
    }

    private static byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static String generateSaltHex(int length) {
        byte[] salt = generateSalt(length);
        return Hex.encodeHexString(salt);
    }

}
