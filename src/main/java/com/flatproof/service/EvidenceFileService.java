package com.flatproof.service;

import com.flatproof.dto.evidence.EvidenceFileResponse;
import com.flatproof.entity.ConditionItem;
import com.flatproof.entity.EvidenceFile;
import com.flatproof.entity.InspectionReport;
import com.flatproof.entity.InspectionStatus;
import com.flatproof.entity.User;
import com.flatproof.repository.ConditionItemRepository;
import com.flatproof.repository.EvidenceFileRepository;
import com.flatproof.repository.InspectionReportRepository;
import com.flatproof.repository.UserRepository;
import com.flatproof.storage.FileStorageService;
import com.flatproof.storage.StoredFileData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvidenceFileService {

    private final EvidenceFileRepository evidenceFileRepository;
    private final InspectionReportRepository inspectionReportRepository;
    private final ConditionItemRepository conditionItemRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final FileHashService fileHashService;

    public EvidenceFileService(EvidenceFileRepository evidenceFileRepository,
                               InspectionReportRepository inspectionReportRepository,
                               ConditionItemRepository conditionItemRepository,
                               UserRepository userRepository,
                               FileStorageService fileStorageService,
                               FileHashService fileHashService) {
        this.evidenceFileRepository = evidenceFileRepository;
        this.inspectionReportRepository = inspectionReportRepository;
        this.conditionItemRepository = conditionItemRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.fileHashService = fileHashService;
    }

    public EvidenceFileResponse uploadReportEvidence(Long reportId,
                                                     MultipartFile file,
                                                     String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        if (report.getStatus() == InspectionStatus.FINALIZED) {
            throw new RuntimeException("Cannot modify finalized report");
        }

        StoredFileData storedFileData = fileStorageService.storeFile(file);
        String fileHash = fileHashService.calculateSha256(storedFileData.getStoragePath());

        EvidenceFile evidenceFile = new EvidenceFile(

                storedFileData.getOriginalFileName(),
                storedFileData.getStoredFileName(),
                storedFileData.getStoragePath(),
                fileHash,
                LocalDateTime.now(),
                report
        );

        EvidenceFile savedEvidence = evidenceFileRepository.save(evidenceFile);
        return mapToResponse(savedEvidence);
    }

    public EvidenceFileResponse uploadConditionItemEvidence(Long itemId,
                                                            MultipartFile file,
                                                            String userEmail) {
        ConditionItem conditionItem = conditionItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Condition item not found"));

        InspectionReport report = conditionItem.getRoomInspection().getReport();
        validateOwnership(report, userEmail);

        if (report.getStatus() == InspectionStatus.FINALIZED) {
            throw new RuntimeException("Cannot modify finalized report");
        }

        StoredFileData storedFileData = fileStorageService.storeFile(file);
        String fileHash = fileHashService.calculateSha256(storedFileData.getStoragePath());

        EvidenceFile evidenceFile = new EvidenceFile(
                storedFileData.getOriginalFileName(),
                storedFileData.getStoredFileName(),
                storedFileData.getStoragePath(),
                fileHash,
                LocalDateTime.now(),
                conditionItem
        );

        EvidenceFile savedEvidence = evidenceFileRepository.save(evidenceFile);
        return mapToResponse(savedEvidence);
    }

    public List<EvidenceFileResponse> getReportEvidence(Long reportId, String userEmail) {
        InspectionReport report = getOwnedReport(reportId, userEmail);

        return evidenceFileRepository.findByReport(report)
                .stream()
                .map(this::mapToResponse)
                .toList();
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

    private EvidenceFileResponse mapToResponse(EvidenceFile evidenceFile) {
        return new EvidenceFileResponse(
                evidenceFile.getId(),
                evidenceFile.getOriginalFileName(),
                evidenceFile.getStoredFileName(),
                evidenceFile.getStoragePath(),
                evidenceFile.getFileHash(),
                evidenceFile.getUploadedAt()
        );
    }
}