package com.flatproof.dto.evidence;

import java.time.LocalDateTime;

public class EvidenceFileResponse {

    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String storagePath;
    private String fileHash;
    private LocalDateTime uploadedAt;

    public EvidenceFileResponse() {
    }

    public EvidenceFileResponse(Long id,
                                String originalFileName,
                                String storedFileName,
                                String storagePath,
                                String fileHash,
                                LocalDateTime uploadedAt) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storagePath = storagePath;
        this.fileHash = fileHash;
        this.uploadedAt = uploadedAt;
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
}