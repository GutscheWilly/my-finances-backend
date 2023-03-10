package com.gutsche.myFinances.service;

import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.enums.LaunchStatus;

import java.math.BigDecimal;
import java.util.List;

public interface LaunchService {

    Launch save(Launch launch);

    Launch update(Launch launch);

    void updateStatus(Launch launch, LaunchStatus launchStatus);

    void delete(Launch launch);

    List<Launch> search(Launch filteredLaunch);

    Launch findById(Long id);

    void validateLaunch(Launch launch);

    BigDecimal sumValuesFromFilteredLaunch(Launch filteredLaunch);
}
