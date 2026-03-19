package com.flatproof.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class healthController {

    @GetMapping("/api/health")
    public String health() {
        return "FlatProof running just fine";
    }
}
