package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.repository.UserRepository;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Profile("test")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImplementation(userRepository);
    }

    @Test
    public void shouldLoginUserSuccessful() {
        String email = "user@gmail.com";
        String password = "123456";

        User user = User.builder().id(1L).name("user").email(email).password(password).build();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User returnedUser = userService.validateLogin(email, password);

        Assertions.assertEquals(returnedUser, user);
    }

    @Test
    public void shouldNotThrowAnyExceptionForValidEmailToRegister() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        String validEmail = "unregisteredUser@gmail.com";

        Assertions.assertDoesNotThrow(() -> userService.validateEmailToRegister(validEmail));
    }

    @Test
    public void shouldThrowBusinessRuleExceptionForInvalidEmailToRegister() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        String invalidEmail = "registeredUser@gmail.com";

        Assertions.assertThrows(BusinessRuleException.class, () -> userService.validateEmailToRegister(invalidEmail));
    }
}
