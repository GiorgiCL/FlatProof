package com.flatproof.service;

import com.flatproof.dto.hash.HashVerificationResponse;
import com.flatproof.entity.InspectionReport;
import com.flatproof.entity.InspectionStatus;
import com.flatproof.entity.User;
import com.flatproof.repository.InspectionReportRepository;
import com.flatproof.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportFinalizationService {

    private final InspectionReportRepository inspectionReportRepository;
    private final UserRepository userRepository;
    private final ReportHashService reportHashService;

    public ReportFinalizationService(InspectionReportRepository inspectionReportRepository,
                                     UserRepository userRepository,
                                     ReportHashService reportHashService) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.userRepository = userRepository;
        this.reportHashService = reportHashService;
    }

    public String finalizeReport(Long reportId, String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        if (report.getStatus() == InspectionStatus.FINALIZED) {
            throw new RuntimeException("Report is already finalized");
        }


        report.setStatus(InspectionStatus.FINALIZED);
        report.setFinalizedAt(LocalDateTime.now());
        String finalHash = reportHashService.generateReportHash(report);
        report.setFinalHash(finalHash);

        inspectionReportRepository.save(report);

        return finalHash;
    }

    public HashVerificationResponse verifyReportHash(Long reportId, String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        if (report.getFinalHash() == null) {
            return new HashVerificationResponse(
                    report.getId(),
                    null,
                    null,
                    false,
                    "Report is not finalized yet"
            );
        }

        String currentHash = reportHashService.generateReportHash(report);
        boolean valid = currentHash.equals(report.getFinalHash());

        return new HashVerificationResponse(
                report.getId(),
                currentHash,
                report.getFinalHash(),
                valid,
                valid ? "Report integrity is valid" : "Report content has been altered"
        );
    }

    private InspectionReport getOwnedReport(Long reportId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InspectionReport report = inspectionReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!report.getProperty().getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have access to this report");
        }

        return report;
    }
}