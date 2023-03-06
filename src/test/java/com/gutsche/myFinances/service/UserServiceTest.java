package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.repository.UserRepository;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import com.gutsche.myFinances.service.exceptions.LoginException;
import com.gutsche.myFinances.service.implementation.UserServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserServiceImplementation userService;

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
    public void shouldThrowLoginExceptionBecauseEmailIsNotRegistered() {
        String unregisteredEmail = "unregisteredUser@gmail.com";
        String password = "123456";

        Mockito.when(userRepository.findByEmail(unregisteredEmail)).thenReturn(Optional.empty());

        LoginException loginException = Assertions.assertThrows(LoginException.class, () -> userService.validateLogin(unregisteredEmail, password));
        Assertions.assertEquals("There's no user registered with this email!", loginException.getMessage());
    }

    @Test
    public void shouldThrowLoginExceptionBecausePasswordIsIncorrect() {
        String registeredEmail = "registeredUser@gmail.com";
        String incorrectPassword = "incorrect password";

        String correctPassword = "correct password";
        User user = User.builder().id(1L).name("user").email(registeredEmail).password(correctPassword).build();

        Mockito.when(userRepository.findByEmail(registeredEmail)).thenReturn(Optional.of(user));

        LoginException loginException = Assertions.assertThrows(LoginException.class, () -> userService.validateLogin(registeredEmail, incorrectPassword));
        Assertions.assertEquals("Incorrect password!", loginException.getMessage());
    }

    @Test
    public void shouldRegisterUserSuccessful() {
        String unregisteredEmail = "unregisteredEmail@gmail.com";
        User user = User.builder().id(1L).name("user").email(unregisteredEmail).password("123456").build();

        Mockito.doNothing().when(userService).validateEmailToRegister(unregisteredEmail);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User returnedUser = userService.registerUser(new User());

        Assertions.assertEquals(returnedUser, user);
    }

    @Test
    public void shouldNotSaveUserWithEmailAlreadyRegistered() {
        String registeredEmail = "registeredEmail@gmail.com";
        User user = User.builder().id(1L).name("user").email(registeredEmail).password("123456").build();

        Mockito.doThrow(BusinessRuleException.class).when(userService).validateEmailToRegister(registeredEmail);
        Mockito.verify(userRepository, Mockito.never()).save(user);

        Assertions.assertThrows(BusinessRuleException.class, () -> userService.registerUser(user));
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
