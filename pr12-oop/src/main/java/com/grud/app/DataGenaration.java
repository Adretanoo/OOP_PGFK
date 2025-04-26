package com.grud.app;

import com.github.javafaker.Faker;
import com.grud.dao.UserDao;
import com.grud.entity.User;
import java.time.LocalDate;

public class DataGenaration {
    public static void main(String[] args) {

        var userDao = UserDao.getInstance();
        var faker = new Faker();

        for (int i = 0; i < 50; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            user.setEmail(faker.internet().emailAddress());
            user.setPhone(faker.phoneNumber().cellPhone());
            user.setAddress(faker.address().fullAddress());
            user.setDateOfBirth(LocalDate.of(1990 + i % 10, (i % 12) + 1, (i % 28) + 1));
            user.setStatus(i % 2 == 0 ? "active" : "inactive");

            var insertedUser = userDao.insert(user);
            System.out.println("Inserted: " + insertedUser);
        }
    }
}
