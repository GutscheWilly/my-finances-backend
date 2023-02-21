package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User validateLogin(String email, String password) {
        return null;
    }

    @Override
    public User registerUser(User user) {
        return null;
    }

    @Override
    public boolean validateEmail(String email) {
        return false;
    }
}
