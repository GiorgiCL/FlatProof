package com.flatproof.dto.inspection;

import com.flatproof.entity.ConditionStatus;

public class ConditionItemResponse {

    private Long id;
    private String itemType;
    private ConditionStatus conditionStatus;
    private String comment;

    public ConditionItemResponse() {
    }

    public ConditionItemResponse(Long id, String itemType, ConditionStatus conditionStatus, String comment) {
        this.id = id;
        this.itemType = itemType;
        this.conditionStatus = conditionStatus;
        this.comment = comment;
    }

    public Long getId() {
        return id;
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
}