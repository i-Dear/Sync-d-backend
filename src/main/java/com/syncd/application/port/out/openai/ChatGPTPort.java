package com.syncd.application.port.out.openai;

import com.syncd.application.port.in.MakeUserstoryUsecase;
import com.syncd.dto.MakeUserStoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ChatGPT 서비스 인터페이스
 *
 * @author : lee
 * @fileName : ChatGPTService
 * @since : 12/29/23
 */

@Service
public interface ChatGPTPort {

    List<Map<String, Object>> modelList();

    MakeUserStoryResponseDto makeUserstory(List<String> senarios);

    Map<String, Object> isValidModel(String modelName);

}