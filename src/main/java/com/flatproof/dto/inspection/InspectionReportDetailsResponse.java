package com.flatproof.dto.inspection;

import com.flatproof.entity.InspectionStatus;

import java.time.LocalDateTime;
import java.util.List;

public class InspectionReportDetailsResponse {

    private Long id;
    private Long propertyId;
    private String propertyTitle;
    private InspectionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime finalizedAt;
    private String createdByEmail;
    private String notes;
    private List<RoomInspectionResponse> rooms;

    public InspectionReportDetailsResponse() {
    }

    public InspectionReportDetailsResponse(Long id,
                                           Long propertyId,
                                           String propertyTitle,
                                           InspectionStatus status,
                                           LocalDateTime createdAt,
                                           LocalDateTime finalizedAt,
                                           String createdByEmail,
                                           String notes,
                                           List<RoomInspectionResponse> rooms) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.status = status;
        this.createdAt = createdAt;
        this.finalizedAt = finalizedAt;
        this.createdByEmail = createdByEmail;
        this.notes = notes;
        this.rooms = rooms;
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

    public List<RoomInspectionResponse> getRooms() {
        return rooms;
    }
}