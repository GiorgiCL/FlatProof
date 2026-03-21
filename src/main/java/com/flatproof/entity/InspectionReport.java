package com.flatproof.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_reports")
public class InspectionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id")
    private Property property;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionStatus status;

    @Column(length = 64)
    private String finalHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finalizedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(length = 2000)
    private String notes;

    @Column(name = "blockchain_tx_hash")
    private String blockchainTxHash;

    @Column(name = "blockchain_record_id")
    private String blockchainId;

    public InspectionReport() {
    }

    public InspectionReport(Long id,
                            Property property,
                            InspectionStatus status,
                            LocalDateTime createdAt,
                            LocalDateTime finalizedAt,
                            User createdBy,
                            String notes,String finalHash,String blockchainTxHash,String blockchainId) {
        this.id = id;
        this.property = property;
        this.status = status;
        this.createdAt = createdAt;
        this.finalizedAt = finalizedAt;
        this.createdBy = createdBy;
        this.notes = notes;
        this.finalHash = finalHash;
        this.blockchainTxHash = blockchainTxHash;
        this.blockchainId = blockchainId;
    }
    public InspectionReport(Property property,
                            InspectionStatus status,
                            LocalDateTime createdAt,
                            LocalDateTime finalizedAt,
                            User createdBy,
                            String notes,String finalHash,String blockchainTxHash,String blockchainId) {
        this.property = property;
        this.status = status;
        this.createdAt = createdAt;
        this.finalizedAt = finalizedAt;
        this.createdBy = createdBy;
        this.notes = notes;
        this.finalHash = finalHash;
        this.blockchainTxHash = blockchainTxHash;
        this.blockchainId = blockchainId;
    }
    public String getBlockchainTxHash() {
        return blockchainTxHash;
    }
    public void setBlockchainTxHash(String blockchainTxHash) {
        this.blockchainTxHash = blockchainTxHash;
    }
    public String getBlockchainRecordId() {
        return blockchainId;
    }
    public void setBlockchainRecordId(String blockchainId) {
        this.blockchainId = blockchainId;
    }
    public String getFinalHash() {
        return finalHash;
    }
    public void setFinalHash(String finalHash) {
        this.finalHash = finalHash;
    }

    public Long getId() {
        return id;
    }

    public Property getProperty() {
        return property;
    }

    public InspectionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinalizedAt() {
        return finalizedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setStatus(InspectionStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setFinalizedAt(LocalDateTime finalizedAt) {
        this.finalizedAt = finalizedAt;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}