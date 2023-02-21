package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;

public interface UserServiceInterface {

    User validateLogin(String email, String password);

    User registerUser(User user);

    boolean validateEmail(String email);
}
