package com.flatproof.dto.report;

import jakarta.validation.constraints.Size;

public class CreateInspectionReportRequest {

    @Size(max = 2000, message = "Notes must be at most 2000 characters")
    private String notes;

    public CreateInspectionReportRequest() {

    }
    public String getNotes() {
        return notes;
    }
}
