package com.syncd.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequestDto {

    private String model;

    private List<MessageDto> messages;

    @Builder
    ChatRequestDto(String model, List<MessageDto> messages) {
        this.model = model;
        this.messages = messages;
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MessageDto {

        private String role;

        private String content;

        @Builder
        MessageDto(String role, String content) {
            this.role = role;
            this.content = content;
        }

    }
}


