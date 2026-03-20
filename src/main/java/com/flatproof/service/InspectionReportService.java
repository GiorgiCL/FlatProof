package com.flatproof.service;

import com.flatproof.dto.report.CreateInspectionReportRequest;
import com.flatproof.dto.report.InspectionReportResponse;
import com.flatproof.entity.InspectionReport;
import com.flatproof.entity.InspectionStatus;
import com.flatproof.entity.Property;
import com.flatproof.entity.User;
import com.flatproof.repository.InspectionReportRepository;
import com.flatproof.repository.PropertyRepository;
import com.flatproof.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectionReportService {

    private final InspectionReportRepository inspectionReportRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public InspectionReportService(InspectionReportRepository inspectionReportRepository,
                                   PropertyRepository propertyRepository,
                                   UserRepository userRepository) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    public InspectionReportResponse createReport(Long propertyId,
                                                 CreateInspectionReportRequest request,
                                                 String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have access to this property");
        }

        InspectionReport report = new InspectionReport(
                property,
                InspectionStatus.DRAFT,
                LocalDateTime.now(),
                null,
                user,
                request.getNotes()
        );

        InspectionReport savedReport = inspectionReportRepository.save(report);
        return mapToResponse(savedReport);
    }

    public InspectionReportResponse getReportById(Long reportId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InspectionReport report = inspectionReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!report.getProperty().getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have access to this report");
        }

        return mapToResponse(report);
    }

    public List<InspectionReportResponse> getReportsByPropertyId(Long propertyId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have access to this property");
        }

        return inspectionReportRepository.findByProperty(property)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private InspectionReportResponse mapToResponse(InspectionReport report) {
        return new InspectionReportResponse(
                report.getId(),
                report.getProperty().getId(),
                report.getProperty().getTitle(),
                report.getStatus(),
                report.getCreatedAt(),
                report.getFinalizedAt(),
                report.getCreatedBy().getEmail(),
                report.getNotes()
        );
    }
}