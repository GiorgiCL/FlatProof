package com.flatproof.repository;

import com.flatproof.entity.InspectionReport;
import com.flatproof.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, Long> {
    List<InspectionReport> findByProperty(Property property);

}
