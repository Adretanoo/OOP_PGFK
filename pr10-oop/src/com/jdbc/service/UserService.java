package com.jdbc.service;

import com.jdbc.dao.UserDao;
import com.jdbc.entity.User;
import java.sql.SQLException;

public class UserService {

    private UserDao userDao;
    private UserValidationService validationService;

    public UserService(UserDao userDao, UserValidationService validationService) {
        this.userDao = userDao;
        this.validationService = validationService;
    }

    public boolean addUser(User user) throws SQLException {
        if (!validationService.validateLogin(user.getLogin())) {
            throw new IllegalArgumentException("Invalid login");
        }
        if (!validationService.validateEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!validationService.validatePassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (user.getPhone() != null && !validationService.validatePhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        userDao.addUser(user);
        return true;
    }
}

