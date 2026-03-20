package com.flatproof.dto.inspection;

import java.util.List;

public class RoomInspectionResponse {

    private Long id;
    private String roomName;
    private List<ConditionItemResponse> items;

    public RoomInspectionResponse() {
    }

    public RoomInspectionResponse(Long id, String roomName, List<ConditionItemResponse> items) {
        this.id = id;
        this.roomName = roomName;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<ConditionItemResponse> getItems() {
        return items;
    }
}