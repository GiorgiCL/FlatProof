package com.flatproof.service;

import com.flatproof.entity.*;
import com.flatproof.repository.ConditionItemRepository;
import com.flatproof.repository.EvidenceFileRepository;
import com.flatproof.repository.RoomInspectionRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportHashService {

    private final RoomInspectionRepository roomInspectionRepository;
    private final ConditionItemRepository conditionItemRepository;
    private final EvidenceFileRepository evidenceFileRepository;

    public ReportHashService(RoomInspectionRepository roomInspectionRepository,
                             ConditionItemRepository conditionItemRepository,
                             EvidenceFileRepository evidenceFileRepository) {
        this.roomInspectionRepository = roomInspectionRepository;
        this.conditionItemRepository = conditionItemRepository;
        this.evidenceFileRepository = evidenceFileRepository;
    }

    public String generateReportHash(InspectionReport report) {
        String canonicalPayload = buildCanonicalPayload(report);
        return sha256(canonicalPayload);
    }

    private String buildCanonicalPayload(InspectionReport report) {
        StringBuilder sb = new StringBuilder();

        sb.append("reportId=").append(report.getId()).append("\n");
        sb.append("propertyId=").append(report.getProperty().getId()).append("\n");
        sb.append("propertyTitle=").append(safe(report.getProperty().getTitle())).append("\n");
        sb.append("propertyAddress=").append(safe(report.getProperty().getAddress())).append("\n");
        sb.append("status=").append(report.getStatus()).append("\n");
        sb.append("createdAt=").append(report.getCreatedAt()).append("\n");
        sb.append("createdBy=").append(safe(report.getCreatedBy().getEmail())).append("\n");
        sb.append("notes=").append(safe(report.getNotes())).append("\n");

        List<RoomInspection> rooms = roomInspectionRepository.findByReport(report)
                .stream()
                .sorted(Comparator.comparing(RoomInspection::getId))
                .toList();

        for (RoomInspection room : rooms) {
            sb.append("roomId=").append(room.getId()).append("\n");
            sb.append("roomName=").append(safe(room.getRoomName())).append("\n");

            List<ConditionItem> items = conditionItemRepository.findByRoomInspection(room)
                    .stream()
                    .sorted(Comparator.comparing(ConditionItem::getId))
                    .toList();

            for (ConditionItem item : items) {
                sb.append("itemId=").append(item.getId()).append("\n");
                sb.append("itemType=").append(safe(item.getItemType())).append("\n");
                sb.append("conditionStatus=").append(item.getConditionStatus()).append("\n");
                sb.append("comment=").append(safe(item.getComment())).append("\n");

                List<EvidenceFile> itemEvidence = evidenceFileRepository.findByConditionItem(item)
                        .stream()
                        .sorted(Comparator.comparing(EvidenceFile::getId))
                        .toList();

                for (EvidenceFile evidenceFile : itemEvidence) {
                    sb.append("itemEvidenceId=").append(evidenceFile.getId()).append("\n");
                    sb.append("itemEvidenceHash=").append(evidenceFile.getFileHash()).append("\n");
                }
            }
        }

        List<EvidenceFile> reportEvidence = evidenceFileRepository.findByReport(report)
                .stream()
                .sorted(Comparator.comparing(EvidenceFile::getId))
                .toList();

        for (EvidenceFile evidenceFile : reportEvidence) {
            sb.append("reportEvidenceId=").append(evidenceFile.getId()).append("\n");
            sb.append("reportEvidenceHash=").append(evidenceFile.getFileHash()).append("\n");
        }

        return sb.toString();
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report hash");
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}