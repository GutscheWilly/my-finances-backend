package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;

public interface UserService {

    User validateLogin(String email, String password);

    User registerUser(User user);

    void validateEmailToRegister(String email);
}
