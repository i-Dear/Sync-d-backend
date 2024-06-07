package com.syncd.application.port.in;

import com.syncd.domain.project.CoreDetails;
import com.syncd.domain.project.Epic;
import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SyncProjectUsecase {
    // ======================================
    // METHOD
    // ======================================
    SyncProjectResponseDto syncProject(String userId, String projectId, int projectStage,
                                       String problem,
                                       String personaInfos,
                                       MultipartFile whyWhatHowImage,
                                       String coreDetails,
                                       MultipartFile businessModelImage,
                                       String epics,
                                       MultipartFile menuTreeImage
                                       );
    // ======================================
    // DTO
    // ======================================

    record SyncProjectRequestDto(
            @NotBlank(message = ValidationMessages.PROJECT_ID_NOT_BLANK)
            String projectId,
            @NotNull(message = ValidationMessages.PROJECT_PROGRESS_NOT_NULL)
            Integer projectStage,
            // Sync 내용, projectStage 마다 필요한 요소들이 존재
            String problem,
            String coreDetails,
            String epics,
            String personaInfos,
            MultipartFile whyWhatHowImage,
            MultipartFile businessModelImage,
            MultipartFile menuTreeImage
    ){}

    record SyncProjectResponseDto(
            String projectId
    ){}
}
