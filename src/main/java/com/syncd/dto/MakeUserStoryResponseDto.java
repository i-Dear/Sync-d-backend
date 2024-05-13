package com.syncd.dto;

import lombok.Data;

import java.util.List;

@Data
public class MakeUserStoryResponseDto {

    private List<EpicDto> epics;

    @Data
    public static class EpicDto {
        private String id;
        private String name;
        private List<UserStoryDto> userStories;
    }
    @Data
    public static class UserStoryDto {
        private int id;
        private String name;
    }
}