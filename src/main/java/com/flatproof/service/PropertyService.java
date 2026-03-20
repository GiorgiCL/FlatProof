package com.flatproof.service;

import com.flatproof.dto.property.CreatePropertyRequest;
import com.flatproof.dto.property.PropertyResponse;
import com.flatproof.entity.Property;
import com.flatproof.entity.User;
import com.flatproof.repository.PropertyRepository;
import com.flatproof.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyService(PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    public PropertyResponse createProperty(CreatePropertyRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = new Property(
                null,
                request.getTitle(),
                request.getAddress(),
                request.getDescription(),
                user
        );

        Property savedProperty = propertyRepository.save(property);
        return mapToResponse(savedProperty);
    }

    public List<PropertyResponse> getMyProperties(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return propertyRepository.findByCreatedBy(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PropertyResponse getMyPropertyById(Long propertyId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have access to this property");
        }

        return mapToResponse(property);
    }

    private PropertyResponse mapToResponse(Property property) {
        return new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getAddress(),
                property.getDescription(),
                property.getCreatedBy().getEmail()
        );
    }
}