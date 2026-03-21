package com.flatproof.dto.blockchain;

public class BlockchainAnchorResponse {

    private Long reportId;
    private String finalHash;
    private String blockchainTxHash;
    private String blockchainRecordId;
    private String message;

    public BlockchainAnchorResponse() {
    }

    public BlockchainAnchorResponse(Long reportId,
                                    String finalHash,
                                    String blockchainTxHash,
                                    String blockchainRecordId,
                                    String message) {
        this.reportId = reportId;
        this.finalHash = finalHash;
        this.blockchainTxHash = blockchainTxHash;
        this.blockchainRecordId = blockchainRecordId;
        this.message = message;
    }

    public Long getReportId() {
        return reportId;
    }

    public String getFinalHash() {
        return finalHash;
    }

    public String getBlockchainTxHash() {
        return blockchainTxHash;
    }

    public String getBlockchainRecordId() {
        return blockchainRecordId;
    }

    public String getMessage() {
        return message;
    }
}