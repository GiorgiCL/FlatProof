package com.flatproof.dto.inspection;

import jakarta.validation.constraints.NotBlank;

public class CreateRoomInspectionRequest {

    @NotBlank(message = "Room name is required")
    private String roomName;

    public CreateRoomInspectionRequest() {
    }

    public String getRoomName() {
        return roomName;
    }
}