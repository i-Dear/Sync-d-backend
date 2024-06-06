package com.syncd.application.port.in;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface GetResultPdfUsecase {
    // ======================================
    // METHOD
    // ======================================
    GetResultPdfUsecaseResponseDto getResultPdfUsecaseProject(String userId, String projectId);
    // ======================================
    // DTO
    // ======================================

    record GetResultPdfUsecaseResponseDto(
            String pdfUrl
    ) {}

}
