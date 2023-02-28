package com.gutsche.myFinances.service.implementation;

import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.repository.LaunchRepository;
import com.gutsche.myFinances.service.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaunchServiceImplementation implements LaunchService {

    private LaunchRepository launchRepository;

    @Autowired
    public LaunchServiceImplementation(LaunchRepository launchRepository) {
        this.launchRepository = launchRepository;
    }

    @Override
    public Launch save(Launch launch) {
        return null;
    }

    @Override
    public Launch update(Launch launch) {
        return null;
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
