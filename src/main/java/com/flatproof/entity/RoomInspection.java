package com.flatproof.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_inspections")
public class RoomInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "report_id")
    private InspectionReport report;

    @Column(nullable = false)
    private String roomName;

    public RoomInspection() {
    }

    public RoomInspection(Long id, InspectionReport report, String roomName) {
        this.id = id;
        this.report = report;
        this.roomName = roomName;
    }
    public RoomInspection(InspectionReport report, String roomName) {
        this.report = report;
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public InspectionReport getReport() {
        return report;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setReport(InspectionReport report) {
        this.report = report;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}