package com.syncd.application.port.out.openai;

import com.syncd.dto.MakeUserStoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface GetChatGptPricePort {

    MakeUserStoryResponseDto getChatGptPricePort(List<String> senarios);
}