package com.gutsche.myFinances.api.resource;

import com.gutsche.myFinances.api.dto.LaunchDTO;
import com.gutsche.myFinances.model.entity.Launch;
import com.gutsche.myFinances.model.entity.User;
import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.entity.enums.LaunchType;
import com.gutsche.myFinances.service.LaunchService;
import com.gutsche.myFinances.service.UserService;
import com.gutsche.myFinances.service.exceptions.BusinessRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/launches")
public class LaunchResource {

    private LaunchService launchService;

    private UserService userService;

    @Autowired
    public LaunchResource(LaunchService launchService, UserService userService) {
        this.launchService = launchService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> save(LaunchDTO launchDTO) {
        try {
            Launch launch = buildLaunch(launchDTO);
            Launch savedLaunch = launchService.save(launch);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLaunch);
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    private Launch buildLaunch(LaunchDTO launchDTO) {
        User user = userService.findById(launchDTO.getUser());
        LaunchType launchType = LaunchType.valueOf(launchDTO.getType());
        LaunchStatus launchStatus = LaunchStatus.valueOf(launchDTO.getStatus());

        return Launch.builder()
                .id(launchDTO.getId())
                .description(launchDTO.getDescription())
                .month(launchDTO.getMonth())
                .year(launchDTO.getYear())
                .value(launchDTO.getValue())
                .user(user)
                .type(launchType)
                .status(launchStatus)
                .build();
    }
}
