package com.gutsche.myFinances.model.repository;

import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.entity.enums.LaunchType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LaunchRepositoryTest {

    @Autowired
    private LaunchRepository launchRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldSaveLaunchSuccessful() {
        Launch launch = buildLaunch();

        Launch savedLaunch = launchRepository.save(launch);

        Assertions.assertNotNull(savedLaunch.getId());
    }

    @Test
    public void shouldDeleteLaunch() {
        Launch launch = buildLaunch();
        Launch savedLaunch = testEntityManager.persist(launch);

        launchRepository.delete(savedLaunch);

        Launch deletedLaunch = testEntityManager.find(Launch.class, savedLaunch.getId());

        Assertions.assertNull(deletedLaunch);
    }

    private static Launch buildLaunch() {
        return Launch.builder()
                .month(1)
                .year(2023)
                .description("Test launch!")
                .value(BigDecimal.valueOf(100))
                .type(LaunchType.REVENUE)
                .status(LaunchStatus.PENDENT)
                .build();
    }
}
