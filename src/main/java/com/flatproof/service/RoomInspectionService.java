package com.flatproof.service;

import com.flatproof.dto.inspection.*;
import com.flatproof.entity.*;
import com.flatproof.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomInspectionService {

    private final InspectionReportRepository inspectionReportRepository;
    private final RoomInspectionRepository roomInspectionRepository;
    private final ConditionItemRepository conditionItemRepository;
    private final UserRepository userRepository;

    public RoomInspectionService(InspectionReportRepository inspectionReportRepository,
                                 RoomInspectionRepository roomInspectionRepository,
                                 ConditionItemRepository conditionItemRepository,
                                 UserRepository userRepository) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.roomInspectionRepository = roomInspectionRepository;
        this.conditionItemRepository = conditionItemRepository;
        this.userRepository = userRepository;
    }

    public RoomInspectionResponse addRoom(Long reportId,
                                          CreateRoomInspectionRequest request,
                                          String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        if (report.getStatus() == InspectionStatus.FINALIZED) {
            throw new RuntimeException("Cannot modify finalized report");
        }

        RoomInspection roomInspection = new RoomInspection(
                report,
                request.getRoomName()
        );

        RoomInspection savedRoom = roomInspectionRepository.save(roomInspection);

        return new RoomInspectionResponse(
                savedRoom.getId(),
                savedRoom.getRoomName(),
                List.of()
        );
    }

    public ConditionItemResponse addConditionItem(Long roomId,
                                                  CreateConditionItemRequest request,
                                                  String userEmail) {
        RoomInspection roomInspection = roomInspectionRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room inspection not found"));

        InspectionReport report = roomInspection.getReport();
        validateOwnership(report, userEmail);

        if (report.getStatus() == InspectionStatus.FINALIZED) {
            throw new RuntimeException("Cannot modify finalized report");
        }

        ConditionItem item = new ConditionItem(
                roomInspection,
                request.getItemType(),
                request.getConditionStatus(),
                request.getComment()
        );

        ConditionItem savedItem = conditionItemRepository.save(item);
        return mapItem(savedItem);
    }

    public InspectionReportDetailsResponse getReportDetails(Long reportId, String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        List<RoomInspectionResponse> rooms = roomInspectionRepository.findByReport(report)
                .stream()
                .map(room -> {
                    List<ConditionItemResponse> items = conditionItemRepository.findByRoomInspection(room)
                            .stream()
                            .map(this::mapItem)
                            .toList();

                    return new RoomInspectionResponse(
                            room.getId(),
                            room.getRoomName(),
                            items
                    );
                })
                .toList();

        return new InspectionReportDetailsResponse(
                report.getId(),
                report.getProperty().getId(),
                report.getProperty().getTitle(),
                report.getStatus(),
                report.getCreatedAt(),
                report.getFinalizedAt(),
                report.getCreatedBy().getEmail(),
                report.getNotes(),
                rooms
        );
    }

    private InspectionReport getOwnedReport(Long reportId, String userEmail) {
        InspectionReport report = inspectionReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        validateOwnership(report, userEmail);
        return report;
    }

    private void validateOwnership(InspectionReport report, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!report.getProperty().getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have access to this report");
        }
    }

    private ConditionItemResponse mapItem(ConditionItem item) {
        return new ConditionItemResponse(
                item.getId(),
                item.getItemType(),
                item.getConditionStatus(),
                item.getComment()
        );
    }
}