package com.flatproof.repository;

import com.flatproof.entity.ConditionItem;
import com.flatproof.entity.EvidenceFile;
import com.flatproof.entity.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceFileRepository extends JpaRepository<EvidenceFile, Long> {
    List<EvidenceFile> findByReport(InspectionReport report);
    List<EvidenceFile> findByConditionItem(ConditionItem conditionItem);
}