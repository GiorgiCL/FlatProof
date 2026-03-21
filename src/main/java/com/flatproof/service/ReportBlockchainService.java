package com.flatproof.service;

import com.flatproof.blockchain.BlockchainAnchorService;
import com.flatproof.dto.blockchain.BlockchainAnchorResponse;
import com.flatproof.dto.blockchain.BlockchainVerificationResponse;
import com.flatproof.entity.InspectionReport;
import com.flatproof.entity.InspectionStatus;
import com.flatproof.entity.User;
import com.flatproof.repository.InspectionReportRepository;
import com.flatproof.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportBlockchainService {

    private final InspectionReportRepository inspectionReportRepository;
    private final UserRepository userRepository;
    private final BlockchainAnchorService blockchainAnchorService;

    public ReportBlockchainService(InspectionReportRepository inspectionReportRepository,
                                   UserRepository userRepository,
                                   BlockchainAnchorService blockchainAnchorService) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.userRepository = userRepository;
        this.blockchainAnchorService = blockchainAnchorService;
    }

    public BlockchainAnchorResponse anchorReport(Long reportId, String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        if (report.getStatus() != InspectionStatus.FINALIZED) {
            throw new RuntimeException("Only finalized reports can be anchored");
        }

        if (report.getFinalHash() == null) {
            throw new RuntimeException("Report final hash is missing");
        }

        if (report.getBlockchainTxHash() != null) {
            throw new RuntimeException("Report is already anchored");
        }

        String blockchainRecordId = String.valueOf(report.getId());
        String txHash = blockchainAnchorService.anchorReport(blockchainRecordId, report.getFinalHash());

        report.setBlockchainTxHash(txHash);
        report.setBlockchainRecordId(blockchainRecordId);
        inspectionReportRepository.save(report);

        return new BlockchainAnchorResponse(
                report.getId(),
                report.getFinalHash(),
                txHash,
                blockchainRecordId,
                "Report anchored successfully"
        );
    }

    public BlockchainVerificationResponse verifyAgainstBlockchain(Long reportId, String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        if (report.getBlockchainRecordId() == null) {
            return new BlockchainVerificationResponse(
                    report.getId(),
                    report.getFinalHash(),
                    null,
                    false,
                    "Report is not anchored on blockchain"
            );
        }

        String blockchainHash = blockchainAnchorService.getAnchoredHash(report.getBlockchainRecordId());
        boolean valid = report.getFinalHash() != null && report.getFinalHash().equals(blockchainHash);

        return new BlockchainVerificationResponse(
                report.getId(),
                report.getFinalHash(),
                blockchainHash,
                valid,
                valid ? "Blockchain verification successful" : "Blockchain verification failed"
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