package com.gutsche.myFinances.model.repository;

import com.gutsche.myFinances.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void checkRegisteredEmail() {
        User user = User.builder().name("user").email("user@gmail.com").build();
        userRepository.save(user);

        boolean isRegisteredEmail = userRepository.existsByEmail(user.getEmail());

        Assertions.assertThat(isRegisteredEmail).isTrue();
    }
}
