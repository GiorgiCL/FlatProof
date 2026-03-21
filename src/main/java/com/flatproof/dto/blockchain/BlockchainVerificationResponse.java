package com.flatproof.dto.blockchain;

public class BlockchainVerificationResponse {

    private Long reportId;
    private String localFinalHash;
    private String blockchainHash;
    private boolean valid;
    private String message;

    public BlockchainVerificationResponse() {
    }

    public BlockchainVerificationResponse(Long reportId,
                                          String localFinalHash,
                                          String blockchainHash,
                                          boolean valid,
                                          String message) {
        this.reportId = reportId;
        this.localFinalHash = localFinalHash;
        this.blockchainHash = blockchainHash;
        this.valid = valid;
        this.message = message;
    }

    public Long getReportId() {
        return reportId;
    }

    public String getLocalFinalHash() {
        return localFinalHash;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}