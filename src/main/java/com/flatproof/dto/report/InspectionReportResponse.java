package com.flatproof.dto.report;

import com.flatproof.entity.InspectionStatus;

import java.time.LocalDateTime;

public class InspectionReportResponse {

    private Long id;
    private Long propertyId;
    private String propertyTitle;
    private InspectionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime finalizedAt;
    private String createdByEmail;
    private String notes;

    public InspectionReportResponse() {
    }

    public InspectionReportResponse(Long id,
                                    Long propertyId,
                                    String propertyTitle,
                                    InspectionStatus status,
                                    LocalDateTime createdAt,
                                    LocalDateTime finalizedAt,
                                    String createdByEmail,
                                    String notes) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.status = status;
        this.createdAt = createdAt;
        this.finalizedAt = finalizedAt;
        this.createdByEmail = createdByEmail;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public InspectionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinalizedAt() {
        return finalizedAt;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public String getNotes() {
        return notes;
    }
}