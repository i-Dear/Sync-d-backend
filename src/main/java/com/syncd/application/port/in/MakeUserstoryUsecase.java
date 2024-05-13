package com.syncd.application.port.in;

import com.syncd.dto.MakeUserStoryResponseDto;
import lombok.Data;

import java.util.List;

public interface MakeUserstoryUsecase {
    MakeUserStoryResponseDto makeUserstory(String userId, String projectId, List<String> scenarios);
}
