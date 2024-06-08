package com.syncd.application.port.in.admin;

import com.syncd.exceptions.ValidationMessages;
import jakarta.validation.constraints.NotBlank;

public interface GetChatgptPriceAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    GetChatgptPriceResponseDto getChatgptPrice(
            @NotBlank(message = ValidationMessages.ADMIN_ID_NOT_BLANK)
            String adminId
    );

    // ======================================
    // DTO
    // ======================================

    record GetChatgptPriceResponseDto(
            String price1,
            String price2,
            String price3,
            String price4,
            String price5
    ) {}
}
