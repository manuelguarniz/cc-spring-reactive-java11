package com.scotiabank.cc.mscollegeapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @RequestMapping
    public String healthCheck() {
        return "OK";
    }
}
