package com.flatproof.dto.inspection;

import com.flatproof.entity.ConditionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateConditionItemRequest {

    @NotBlank(message = "Item type is required")
    private String itemType;

    @NotNull(message = "Condition status is required")
    private ConditionStatus conditionStatus;

    @Size(max = 1000, message = "Comment must be at most 1000 characters")
    private String comment;

    public CreateConditionItemRequest() {
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