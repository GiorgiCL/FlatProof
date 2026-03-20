package com.flatproof.controller;

import com.flatproof.dto.property.CreatePropertyRequest;
import com.flatproof.dto.property.PropertyResponse;
import com.flatproof.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping
    public PropertyResponse createProperty(@Valid @RequestBody CreatePropertyRequest request,
                                           Authentication authentication) {
        String userEmail = authentication.getName();
        return propertyService.createProperty(request, userEmail);
    }

    @GetMapping
    public List<PropertyResponse> getMyProperties(Authentication authentication) {
        String userEmail = authentication.getName();
        return propertyService.getMyProperties(userEmail);
    }

    @GetMapping("/{propertyId}")
    public PropertyResponse getMyPropertyById(@PathVariable Long propertyId,
                                              Authentication authentication) {
        String userEmail = authentication.getName();
        return propertyService.getMyPropertyById(propertyId, userEmail);
    }
}