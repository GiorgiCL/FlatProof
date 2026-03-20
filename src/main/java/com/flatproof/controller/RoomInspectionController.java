package com.flatproof.controller;

import com.flatproof.dto.inspection.*;
import com.flatproof.service.RoomInspectionService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoomInspectionController {

    private final RoomInspectionService roomInspectionService;

    public RoomInspectionController(RoomInspectionService roomInspectionService) {
        this.roomInspectionService = roomInspectionService;
    }

    @PostMapping("/reports/{reportId}/rooms")
    public RoomInspectionResponse addRoom(@PathVariable Long reportId,
                                          @Valid @RequestBody CreateRoomInspectionRequest request,
                                          Authentication authentication) {
        String userEmail = authentication.getName();
        return roomInspectionService.addRoom(reportId, request, userEmail);
    }

    @PostMapping("/rooms/{roomId}/items")
    public ConditionItemResponse addConditionItem(@PathVariable Long roomId,
                                                  @Valid @RequestBody CreateConditionItemRequest request,
                                                  Authentication authentication) {
        String userEmail = authentication.getName();
        return roomInspectionService.addConditionItem(roomId, request, userEmail);
    }

    @GetMapping("/reports/{reportId}/details")
    public InspectionReportDetailsResponse getReportDetails(@PathVariable Long reportId,
                                                            Authentication authentication) {
        String userEmail = authentication.getName();
        return roomInspectionService.getReportDetails(reportId, userEmail);
    }
}