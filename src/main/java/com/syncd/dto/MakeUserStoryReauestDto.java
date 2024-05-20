package com.syncd.dto;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MakeUserStoryReauestDto {
    @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
    private String projectId;
    @NotNull(message = ValidationMessages.SCENARIOS_NOT_NULL) @NotEmpty(message = ValidationMessages.SCENARIOS_NOT_EMPTY)
    private List<String> scenario;
}