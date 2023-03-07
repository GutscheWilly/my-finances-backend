package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.entity.enums.LaunchType;
import com.gutsche.myFinances.model.repository.LaunchRepository;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import com.gutsche.myFinances.service.implementation.LaunchServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LaunchServiceTest {

    @SpyBean
    private LaunchServiceImplementation launchService;

    @MockBean
    private LaunchRepository launchRepository;

    @Test
    public void shouldSaveLaunchSuccessful() {
        Launch launch = buildLaunch();

        Mockito.doNothing().when(launchService).validateLaunch(launch);

        Launch savedLaunch = buildLaunch();
        savedLaunch.setId(1L);

        Mockito.when(launchRepository.save(launch)).thenReturn(savedLaunch);

        Launch returnedLaunch = launchService.save(launch);

        Assertions.assertEquals(returnedLaunch, savedLaunch);
    }

    @Test
    public void shouldNotSaveInvalidLaunch() {
        Launch invalidLaunch = buildLaunch();

        Mockito.doThrow(BusinessRuleException.class).when(launchService).validateLaunch(invalidLaunch);

        Assertions.assertThrows(BusinessRuleException.class, () -> launchService.save(invalidLaunch));

        Mockito.verify(launchRepository, Mockito.never()).save(invalidLaunch);
    }

    @Test
    public void shouldUpdateLaunch() {
        Launch launch = buildLaunch();
        launch.setId(1L);

        Mockito.doNothing().when(launchService).validateLaunch(launch);

        Mockito.when(launchRepository.save(launch)).thenReturn(launch);

        Launch updatedLaunch = launchService.update(launch);

        Assertions.assertEquals(updatedLaunch, launch);

        Mockito.verify(launchRepository, Mockito.times(1)).save(launch);
    }

    @Test
    public void shouldNotUpdateLaunchWithoutId() {
        Launch launchWithoutId = buildLaunch();

        Assertions.assertThrows(NullPointerException.class, () -> launchService.update(launchWithoutId));

        Mockito.verify(launchRepository, Mockito.never()).save(launchWithoutId);
    }

    @Test
    public void shouldDeleteLaunch() {
        Launch launch = buildLaunch();
        launch.setId(1L);

        launchService.delete(launch);

        Mockito.verify(launchRepository).delete(launch);
    }

    private static Launch buildLaunch() {
        return Launch.builder()
                .description("Test launch")
                .month(2)
                .year(2023)
                .value(BigDecimal.valueOf(100))
                .user(User.builder().id(1L).name("user").email("user@gmail.com").password("1234").build())
                .type(LaunchType.REVENUE)
                .build();
    }
}
