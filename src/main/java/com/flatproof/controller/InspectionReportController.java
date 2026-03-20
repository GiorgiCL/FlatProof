package com.flatproof.controller;

import com.flatproof.dto.report.CreateInspectionReportRequest;
import com.flatproof.dto.report.InspectionReportResponse;
import com.flatproof.service.InspectionReportService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InspectionReportController {

    private final InspectionReportService inspectionReportService;

    public InspectionReportController(InspectionReportService inspectionReportService) {
        this.inspectionReportService = inspectionReportService;
    }

    @PostMapping("/properties/{propertyId}/reports")
    public InspectionReportResponse createReport(@PathVariable Long propertyId,
                                                 @Valid @RequestBody CreateInspectionReportRequest request,
                                                 Authentication authentication) {
        String userEmail = authentication.getName();
        return inspectionReportService.createReport(propertyId, request, userEmail);
    }

    @GetMapping("/reports/{reportId}")
    public InspectionReportResponse getReportById(@PathVariable Long reportId,
                                                  Authentication authentication) {
        String userEmail = authentication.getName();
        return inspectionReportService.getReportById(reportId, userEmail);
    }

    @GetMapping("/properties/{propertyId}/reports")
    public List<InspectionReportResponse> getReportsByPropertyId(@PathVariable Long propertyId,
                                                                 Authentication authentication) {
        String userEmail = authentication.getName();
        return inspectionReportService.getReportsByPropertyId(propertyId, userEmail);
    }
}