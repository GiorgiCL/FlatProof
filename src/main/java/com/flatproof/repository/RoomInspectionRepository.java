package com.flatproof.repository;

import com.flatproof.entity.InspectionReport;
import com.flatproof.entity.RoomInspection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomInspectionRepository extends JpaRepository<RoomInspection, Long> {
    List<RoomInspection> findByReport(InspectionReport report);
}