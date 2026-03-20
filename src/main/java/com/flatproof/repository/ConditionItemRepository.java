package com.flatproof.repository;

import com.flatproof.entity.ConditionItem;
import com.flatproof.entity.RoomInspection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionItemRepository extends JpaRepository<ConditionItem, Long> {
    List<ConditionItem> findByRoomInspection(RoomInspection roomInspection);
}