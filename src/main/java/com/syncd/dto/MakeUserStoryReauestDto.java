package com.syncd.dto;

import lombok.Data;

import java.util.List;

@Data
public class MakeUserStoryReauestDto {
    private String projectId;
    private List<String> senario;

}