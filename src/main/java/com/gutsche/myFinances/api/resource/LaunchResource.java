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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> save(@RequestBody LaunchDTO launchDTO) {
        try {
            Launch launch = buildLaunch(launchDTO);
            Launch savedLaunch = launchService.save(launch);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLaunch);
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LaunchDTO launchDTO) {
        try {
            Launch currentLaunch = launchService.findById(id);

            Launch modifiedLaunch = buildLaunch(launchDTO);
            modifiedLaunch.setId(currentLaunch.getId());

            Launch updatedLaunch = launchService.update(modifiedLaunch);
            return ResponseEntity.ok().body(updatedLaunch);
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateLaunchStatusDTO updateLaunchStatusDTO) {
        try {
            Launch launch = launchService.findById(id);
            LaunchStatus updatedStatus = LaunchStatus.valueOf(updateLaunchStatusDTO.getStatus());

            launchService.updateStatus(launch, updatedStatus);
            return ResponseEntity.ok().body(launch);
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body("Invalid status!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Launch launch = launchService.findById(id);
            launchService.delete(launch);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> search(@RequestBody LaunchDTO launchDTO) {
        try {
            Launch filteredLaunch = buildLaunch(launchDTO);
            List<Launch> foundLaunches = launchService.search(filteredLaunch);
            return ResponseEntity.ok().body(foundLaunches);
        }
        catch (BusinessRuleException businessRuleException) {
            return ResponseEntity.badRequest().body(businessRuleException.getMessage());
        }
    }

    private Launch buildLaunch(LaunchDTO launchDTO) {
        User user = userService.findById(launchDTO.getUser());
        LaunchType type = null;
        LaunchStatus status = null;

        if (launchDTO.getType() != null) {
            type = LaunchType.valueOf(launchDTO.getType());
        }

        if (launchDTO.getStatus() != null) {
            status = LaunchStatus.valueOf(launchDTO.getStatus());
        }

        return Launch.builder()
                .id(launchDTO.getId())
                .description(launchDTO.getDescription())
                .month(launchDTO.getMonth())
                .year(launchDTO.getYear())
                .value(launchDTO.getValue())
                .user(user)
                .type(type)
                .status(status)
                .build();
    }
}
