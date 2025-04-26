package com.jdbc.service;

import com.jdbc.dao.UserDao;
import com.jdbc.entity.User;
import java.sql.SQLException;

public class AuthenticationService {

    private UserDao userDao;

    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User authenticate(String login, String password) throws SQLException {
        User user = userDao.getUserByLogin(login);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
