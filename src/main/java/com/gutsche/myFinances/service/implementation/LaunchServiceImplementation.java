package com.gutsche.myFinances.service.implementation;

import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.repository.LaunchRepository;
import com.gutsche.myFinances.service.LaunchService;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LaunchServiceImplementation implements LaunchService {

    private LaunchRepository launchRepository;

    @Autowired
    public LaunchServiceImplementation(LaunchRepository launchRepository) {
        this.launchRepository = launchRepository;
    }

    @Override
    @Transactional
    public Launch save(Launch launch) {
        validateLaunch(launch);
        launch.setStatus(LaunchStatus.PENDENT);
        return launchRepository.save(launch);
    }

    @Override
    @Transactional
    public Launch update(Launch launch) {
        Objects.requireNonNull(launch.getId());
        validateLaunch(launch);
        return launchRepository.save(launch);
    }

    @Override
    public void updateStatus(Launch launch, LaunchStatus launchStatus) {
        launch.setStatus(launchStatus);
        update(launch);
    }

    @Override
    @Transactional
    public void delete(Launch launch) {
        Objects.requireNonNull(launch.getId());
        launchRepository.delete(launch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Launch> search(Launch filteredLaunch) {
        Example<Launch> launchExample = Example.of(
                filteredLaunch,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );

        return launchRepository.findAll(launchExample);
    }

    @Override
    public Launch findById(Long id) {
        Optional<Launch> launchOptional = launchRepository.findById(id);
        return launchOptional.orElseThrow(() -> new BusinessRuleException("Launch not found by ID!"));
    }

     @Override
    public BigDecimal sumValuesFromFilteredLaunch(Launch filteredLaunch) {
        List<Launch> foundLaunches = search(filteredLaunch);

        return foundLaunches.stream()
                .map(Launch::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void validateLaunch(Launch launch) {
        if (isNull(launch.getDescription()) || isInvalidDescription(launch.getDescription())) {
            throw new BusinessRuleException("Enter a description!");
        }

        if (isNull(launch.getMonth()) || isInvalidMonth(launch.getMonth())) {
            throw new BusinessRuleException("Enter a valid month!");
        }

        if (isNull(launch.getYear()) || isInvalidYear(launch.getYear())) {
            throw new BusinessRuleException("Enter a valid year!");
        }

        if (isNull(launch.getValue()) || isInvalidValue(launch.getValue())) {
            throw new BusinessRuleException("Enter a valid value!");
        }

        if (isNull(launch.getUser()) || isInvalidUser(launch.getUser())) {
            throw new BusinessRuleException("Enter a valid user!");
        }

        if (isNull(launch.getType())) {
            throw new BusinessRuleException("Enter a type of launch!");
        }
    }

    private boolean isNull(Object object) {
        return object == null;
    }

    private boolean isInvalidDescription(String description) {
        return description.trim().equals("");
    }

    private boolean isInvalidMonth(Integer month) {
        return month < 1 || month > 12;
    }

    private boolean isInvalidYear(Integer year) {
        return year.toString().length() != 4;
    }

    private boolean isInvalidValue(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 1;
    }

    private boolean isInvalidUser(User user) {
        return isNull(user.getId());
    }
}
