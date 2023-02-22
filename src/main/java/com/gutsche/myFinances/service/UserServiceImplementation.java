package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.repository.UserRepository;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
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
    public void validateEmailToRegister(String email) {
        boolean isRegisteredEmail = userRepository.existsByEmail(email);

        if (isRegisteredEmail) {
            throw new BusinessRuleException("This email has already been registered by other user!");
        }
    }
}
