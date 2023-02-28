package com.gutsche.myFinances.service.implementation;

import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.repository.LaunchRepository;
import com.gutsche.myFinances.service.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
        return launchRepository.save(launch);
    }

    @Override
    @Transactional
    public Launch update(Launch launch) {
        Objects.requireNonNull(launch.getId());
        return launchRepository.save(launch);
    }

    @Override
    public void updateStatus(Launch launch, LaunchStatus launchStatus) {

    }

    @Override
    public void delete(Launch launch) {

    }

    @Override
    public List<Launch> search(Launch filteredLaunch) {
        return null;
    }
}
