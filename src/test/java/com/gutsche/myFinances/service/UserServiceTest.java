package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.repository.UserRepository;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Profile("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldNotThrowAnyExceptionForValidEmailToRegister() {
        userRepository.deleteAll();

        String validEmail = "user@gmail.com";

        Assertions.assertDoesNotThrow(() -> userService.validateEmailToRegister(validEmail));
    }

    @Test
    public void shouldThrownBusinessRuleExceptionForInvalidEmailToRegister() {
        User user = User.builder().name("user").email("user@gmail.com").password("123456").build();
        userRepository.save(user);

        String invalidEmail = user.getEmail();

        Assertions.assertThrows(BusinessRuleException.class, () -> userService.validateEmailToRegister(invalidEmail));
    }
}
