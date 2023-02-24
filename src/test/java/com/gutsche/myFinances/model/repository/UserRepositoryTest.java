package com.gutsche.myFinances.model.repository;

import com.gutsche.myFinances.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Profile("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void checkRegisteredEmail() {
        resetContext();
        User user = createUser();
        userRepository.save(user);

        String registeredEmail = user.getEmail();
        boolean isRegisteredEmail = userRepository.existsByEmail(registeredEmail);

        Assertions.assertThat(isRegisteredEmail).isTrue();
    }

    @Test
    public void checkUnregisteredEmail() {
        resetContext();

        String unregisteredEmail = "user@gmail.com";
        boolean isRegisteredEmail = userRepository.existsByEmail(unregisteredEmail);

        Assertions.assertThat(isRegisteredEmail).isFalse();
    }

    @Test
    public void shouldCreateUserIdAfterRegister() {
        resetContext();
        User user = createUser();

        User userAfterRegister = userRepository.save(user);
        Long id = userAfterRegister.getId();

        Assertions.assertThat(id).isNotNull();
    }

    @Test
    public void shouldFindRegisteredUserByEmail() {
        resetContext();
        User user = createUser();
        userRepository.save(user);

        String registeredEmail = user.getEmail();
        boolean isUserFoundByEmail = userRepository.findByEmail(registeredEmail).isPresent();

        Assertions.assertThat(isUserFoundByEmail).isTrue();
    }

    @Test
    public void shouldNotFindUserByEmail() {
        resetContext();

        String unregisteredEmail = "user@gmail.com";
        boolean isUserFoundByEmail = userRepository.findByEmail(unregisteredEmail).isPresent();

        Assertions.assertThat(isUserFoundByEmail).isFalse();
    }

    private static User createUser() {
        return User.builder()
                .name("user")
                .email("user@gmail.com")
                .password("123456")
                .build();
    }

    private void resetContext() {
        userRepository.deleteAll();
    }
}
