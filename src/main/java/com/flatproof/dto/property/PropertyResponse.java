package com.flatproof.dto.property;

public class PropertyResponse {

    private Long id;
    private String title;
    private String address;
    private String description;
    private String createdByEmail;

    public PropertyResponse() {
    }

    public PropertyResponse(Long id, String title, String address, String description, String createdByEmail) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.description = description;
        this.createdByEmail = createdByEmail;
    }

    public Long getId() {
        return id;
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

    public String getCreatedByEmail() {
        return createdByEmail;
    }
}