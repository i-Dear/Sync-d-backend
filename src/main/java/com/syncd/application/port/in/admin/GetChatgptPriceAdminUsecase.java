package com.syncd.application.port.in.admin;

public interface GetChatgptPriceAdminUsecase {

    // ======================================
    // METHOD
    // ======================================
    GetChatgptPriceResponseDto getChatgptPrice();

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
