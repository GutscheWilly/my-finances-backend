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
        User user = User.builder().name("user").email("user@gmail.com").password("123456").build();
        userRepository.save(user);

        String registeredEmail = user.getEmail();
        boolean isRegisteredEmail = userRepository.existsByEmail(registeredEmail);

        Assertions.assertThat(isRegisteredEmail).isTrue();
    }

    @Test
    public void checkUnregisteredEmail() {
        userRepository.deleteAll();

        String unregisteredEmail = "user@gmail.com";
        boolean isRegisteredEmail = userRepository.existsByEmail(unregisteredEmail);

        Assertions.assertThat(isRegisteredEmail).isFalse();
    }
}
