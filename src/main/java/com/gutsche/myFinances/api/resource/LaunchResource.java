package com.gutsche.myFinances.api.resource;

import com.gutsche.myFinances.service.LaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/launches")
public class LaunchResource {

    private LaunchService launchService;

    @Autowired
    public LaunchResource(LaunchService launchService) {
        this.launchService = launchService;
    }
}
