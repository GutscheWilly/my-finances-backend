package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.repository.LaunchRepository;
import com.gutsche.myFinances.service.implementation.LaunchServiceImplementation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LaunchServiceTest {

    @SpyBean
    private LaunchServiceImplementation launchService;

    @MockBean
    private LaunchRepository launchRepository;
}
