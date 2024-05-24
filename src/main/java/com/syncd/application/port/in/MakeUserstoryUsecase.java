package com.syncd.application.port.in;

import com.syncd.dto.MakeUserStoryResponseDto;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

public interface MakeUserstoryUsecase {
    MakeUserStoryResponseDto makeUserstory(String userId,
                                           String projectId,
                                           List<String> scenarios);
}
