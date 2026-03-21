package com.flatproof.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evidence_files")
public class EvidenceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false, length = 64)
    private String fileHash;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private InspectionReport report;

    @ManyToOne
    @JoinColumn(name = "condition_item_id")
    private ConditionItem conditionItem;

    public EvidenceFile() {
    }

    public EvidenceFile(Long id,
                        String originalFileName,
                        String storedFileName,
                        String storagePath,
                        String fileHash,
                        LocalDateTime uploadedAt,
                        InspectionReport report,
                        ConditionItem conditionItem) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storagePath = storagePath;
        this.fileHash = fileHash;
        this.uploadedAt = uploadedAt;
        this.report = report;
        this.conditionItem = conditionItem;
    }
    public EvidenceFile(String originalFileName, String storedFileName, String storagePath,
                        String fileHash, LocalDateTime uploadedAt, InspectionReport report) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storagePath = storagePath;
        this.fileHash = fileHash;
        this.uploadedAt = uploadedAt;
        this.report = report;
        this.conditionItem = null;
    }

    public EvidenceFile(String originalFileName, String storedFileName, String storagePath,
                        String fileHash, LocalDateTime uploadedAt, ConditionItem conditionItem) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storagePath = storagePath;
        this.fileHash = fileHash;
        this.uploadedAt = uploadedAt;
        this.report = null;
        this.conditionItem = conditionItem;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getFileHash() {
        return fileHash;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public InspectionReport getReport() {
        return report;
    }

    public ConditionItem getConditionItem() {
        return conditionItem;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setReport(InspectionReport report) {
        this.report = report;
    }

    public void setConditionItem(ConditionItem conditionItem) {
        this.conditionItem = conditionItem;
    }
}