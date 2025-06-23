package com.scotiabank.cc.mscollegeapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "API de salud de aplicación" )
public class HealthController {
    @GetMapping
    public String healthCheck() {
        return "OK";
    }
}
