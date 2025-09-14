package com.itmo.nxzage.client.auth;

import com.itmo.nxzage.client.commands.Command;

public class AuthService {
    private User currentUser;
    private boolean loggedIn;

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void authCommand(Command command) {
        if (command.getName().equals("register")) {
            if (loggedIn) {
                throw new UnsupportedOperationException("You should log out before creating new user");
            }
            return;
        }
        if (!loggedIn) {
            throw new UnsupportedOperationException("You should be authorized to send request");
        }

        command.applyArg("username", currentUser.name());
        command.applyArg("user_password", currentUser.password());
    }
}
