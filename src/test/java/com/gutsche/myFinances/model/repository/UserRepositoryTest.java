package com.gutsche.myFinances.model.repository;

import com.gutsche.myFinances.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void checkRegisteredEmail() {
        User user = createAndPersistUser();

        String registeredEmail = user.getEmail();
        boolean isRegisteredEmail = userRepository.existsByEmail(registeredEmail);

        Assertions.assertTrue(isRegisteredEmail);
    }

    @Test
    public void checkUnregisteredEmail() {
        String unregisteredEmail = "user@gmail.com";
        boolean isRegisteredEmail = userRepository.existsByEmail(unregisteredEmail);

        Assertions.assertFalse(isRegisteredEmail);
    }

    @Test
    public void shouldCreateUserIdAfterRegister() {
        User user = createUser();
        User userAfterRegister = userRepository.save(user);
        Long id = userAfterRegister.getId();

        Assertions.assertNotNull(id);
    }

    @Test
    public void shouldFindRegisteredUserByEmail() {
        User user = createAndPersistUser();

        String registeredEmail = user.getEmail();
        boolean isUserFoundByEmail = userRepository.findByEmail(registeredEmail).isPresent();

        Assertions.assertTrue(isUserFoundByEmail);
    }

    @Test
    public void shouldNotFindUserByEmail() {
        String unregisteredEmail = "user@gmail.com";
        boolean isUserFoundByEmail = userRepository.findByEmail(unregisteredEmail).isPresent();

        Assertions.assertFalse(isUserFoundByEmail);
    }

    private User createAndPersistUser() {
        User user = createUser();
        return testEntityManager.persist(user);
    }

    private static User createUser() {
        return User.builder()
                .name("user")
                .email("user@gmail.com")
                .password("123456")
                .build();
    }
}
