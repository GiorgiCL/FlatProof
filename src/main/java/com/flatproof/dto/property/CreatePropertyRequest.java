package com.flatproof.dto.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePropertyRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Address is required")
    private String address;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    public CreatePropertyRequest() {
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}