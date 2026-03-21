package com.flatproof.dto.hash;

public class HashVerificationResponse {

    private Long reportId;
    private String currentHash;
    private String storedFinalHash;
    private boolean valid;
    private String message;

    public HashVerificationResponse() {
    }

    public HashVerificationResponse(Long reportId,
                                    String currentHash,
                                    String storedFinalHash,
                                    boolean valid,
                                    String message) {
        this.reportId = reportId;
        this.currentHash = currentHash;
        this.storedFinalHash = storedFinalHash;
        this.valid = valid;
        this.message = message;
    }

    public Long getReportId() {
        return reportId;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public String getStoredFinalHash() {
        return storedFinalHash;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}