package com.flatproof.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "condition_items")
public class ConditionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_inspection_id")
    private RoomInspection roomInspection;

    @Column(nullable = false)
    private String itemType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionStatus conditionStatus;

    @Column(length = 1000)
    private String comment;

    public ConditionItem() {
    }

    public ConditionItem(Long id,
                         RoomInspection roomInspection,
                         String itemType,
                         ConditionStatus conditionStatus,
                         String comment) {
        this.id = id;
        this.roomInspection = roomInspection;
        this.itemType = itemType;
        this.conditionStatus = conditionStatus;
        this.comment = comment;
    }
    public ConditionItem(RoomInspection roomInspection, String itemType, ConditionStatus conditionStatus, String comment) {
        this.roomInspection = roomInspection;
        this.itemType = itemType;
        this.conditionStatus = conditionStatus;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public RoomInspection getRoomInspection() {
        return roomInspection;
    }

    public String getItemType() {
        return itemType;
    }

    public ConditionStatus getConditionStatus() {
        return conditionStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setRoomInspection(RoomInspection roomInspection) {
        this.roomInspection = roomInspection;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setConditionStatus(ConditionStatus conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}