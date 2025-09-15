package com.itmo.nxzage.server.services.auth;

import java.util.NoSuchElementException;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.common.util.exceptions.ValidationException;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.exceptions.AuthenticationException;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.services.db.AuthModule;
import com.itmo.nxzage.server.services.db.models.Credential;

public class AuthenticationService {
    private static final String PEPER = "c4a42904c649b1bb0ce1b24275af8224";
    private static final AuthModule authDataModule;
    private static Logger logger = ServerLogger.getLogger("AuthService");
    private static HashService hasher;
    
    static {
        authDataModule = new AuthModule();
        hasher = new SHA512();
    }

    public User auth(Packet request) throws AuthenticationException {
        if (!request.getType().equals(PacketType.REQUEST)) {
            throw new IllegalArgumentException("Authentication can be provided only for REQUEST packets, not for %s".formatted(request.getType()));
        }
        logger.info("Auth %s request started".formatted(request.getInteractionID()));
        DataContainer payload = request.getPayload();
        checkAuthHeaders(payload);
        String username = payload.get("username", String.class);
        String password = payload.get("user_password", String.class);
        
        Credential credential = getCredentials(username);    
        String passwordHash = getPasswordHash(password, credential.getSalt()); 
        if (passwordHash.equals(credential.getPasswordHash())) {
            logger.info("Request %s authenticated successfully!".formatted(request.getInteractionID().toString()));
            return new User(credential.getUserId(), username);
        } else {
            logger.info("Request %s authentication failed: wrong password".formatted(request.getInteractionID().toString()));
            throw new AuthenticationException("Authentication failed: wrong password");
        }
    }

    public void registerNewUser(Packet request) throws AuthenticationException {
        if (!request.getType().equals(PacketType.REGISTER_REQUEST)) {
            throw new IllegalArgumentException("Authentication can be provided only for REQUEST packets, not for %s".formatted(request.getType()));
        }
        logger.info("Auth %s request started".formatted(request.getInteractionID()));
        DataContainer payload = request.getPayload();
        checkAuthHeaders(payload);
        String username = payload.get("username", String.class);
        String password = payload.get("user_password", String.class);

        String salt = hasher.generateSault();
        String passwordHash = getPasswordHash(password, salt);
        registerUser(username, passwordHash, salt);
    }

    public void checkCommandAccess(PersonStorageCommand command, User user) {

    }
        
    private Credential getCredentials(Integer id) throws AuthenticationException {
        try {
            return authDataModule.getUserCredentials(id);
        } catch (NoSuchElementException e) {
            logger.warning("Failed to get credentials by id=%d".formatted(id));
            throw new AuthenticationException("Failed to get credentials");
        }
    }

    private Credential getCredentials(String username) throws AuthenticationException {
        try {
            return authDataModule.getUserCredentials(username);
        } catch (NoSuchElementException e) {
            logger.warning("Failed to get credentials by username=%s".formatted(username));
            throw new AuthenticationException("Failed to get credentials");
        }
    }
    
    private Integer checkUserExisted(String username) throws AuthenticationException {
        try {
            User user = authDataModule.getUser(username);
            return user.getId();
        } catch (NoSuchElementException e) {
            throw new AuthenticationException("No user with username=%s found".formatted(username));
        } 
    }

    private void checkUserIsNotExisted(String username) throws AuthenticationException {
        try {
            authDataModule.getUser(username);
            throw new AuthenticationException("User with name=%s already exists".formatted(username));
        } catch (NoSuchElementException e) {
            return;
        }
    }

    private String getPasswordHash(String password, String salt) {
        return hasher.getHash(password + PEPER + salt);
    }

    private void checkAuthHeaders(DataContainer payload) throws AuthenticationException {
        try {
            payload.assertType("username", String.class);
            logger.info("Username header existing check - SUCCESS");
            payload.assertType("user_password", String.class);
            logger.info("UserPassword header existing check - SUCCESS");
        } catch (ValidationException e) {
            logger.warning("Request %s doesn\'t contain neccessary headers - AUTH FAILED");
            throw new AuthenticationException("Packet doesn\'t contain neccessary headers: " + e.getMessage());
        }
    }

    private void registerUser(String username, String passwordHash, String salt) throws AuthenticationException {
        try {
            authDataModule.registerUser(username, passwordHash, salt);
        } catch (RuntimeException e) {
            logger.info("FAiled to register user: " + e.getMessage());
            throw new AuthenticationException("Failed to register user");
        }
    }
}
