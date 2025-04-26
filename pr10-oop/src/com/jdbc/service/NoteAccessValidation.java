package com.jdbc.service;

import com.jdbc.entity.User;
import java.sql.SQLException;

public class NoteAccessValidation {

    private AuthenticationService authenticationService;

    public NoteAccessValidation(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean hasAccess(String login, String password, String roleRequired) throws SQLException {
        User user = authenticationService.authenticate(login, password);
        if (user != null) {
            return checkUserRole(user, roleRequired);
        }
        return false;
    }

    private boolean checkUserRole(User user, String roleRequired) {
        String[] roleParts = user.getEmail().split("@");
        return roleParts[0].equals(roleRequired);
    }
}

