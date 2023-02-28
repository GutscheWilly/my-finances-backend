package com.gutsche.myFinances.service.implementation;

import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.repository.UserRepository;
import com.gutsche.myFinances.service.UserService;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import com.gutsche.myFinances.service.exceptions.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User validateLogin(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new LoginException("There's no user registered with this email!"));

        if (checkPassword(user, password)) {
            return user;
        }
        throw new LoginException("Incorrect password!");
    }

    private boolean checkPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        validateEmailToRegister(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void validateEmailToRegister(String email) {
        boolean isRegisteredEmail = userRepository.existsByEmail(email);

        if (isRegisteredEmail) {
            throw new BusinessRuleException("This email has already been registered by other user!");
        }
    }
}
