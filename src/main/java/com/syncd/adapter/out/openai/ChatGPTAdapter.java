package com.syncd.adapter.out.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syncd.application.port.out.openai.ChatGPTPort;
import com.syncd.dto.ChatRequestDto;
import com.syncd.dto.MakeUserStoryResponseDto;
import com.syncd.dto.OpenAIToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGPTAdapter implements ChatGPTPort {
    private final ChatGPTConfig chatGPTConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    @Value("${spring.security.openai.model}")
    private String model;

    @Value("${spring.security.openai.promptForEpic}")
    private String promptForEpic;

    @Value("${spring.security.openai.promptForUserstory}")
    private  String promptForUserStory;

    @Override
    public MakeUserStoryResponseDto makeUserstory(List<String> scenario) {
        OpenAIToken finalToken = new OpenAIToken();
        try {
            MakeUserStoryResponseDto responseDto = promptUserStory(finalToken, om, scenario);
            System.out.println(finalToken);
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Retryable(value = { NullPointerException.class }, maxAttempts = 2, recover = "recoverPromptUserStory")
    private MakeUserStoryResponseDto promptUserStory(OpenAIToken finalToken, ObjectMapper om, List<String> scenario) throws JsonProcessingException,NullPointerException {
        Logger logger = LoggerFactory.getLogger(this.getClass());
            String requestTextForEpic = createRequestForEpic(scenario);
            Map<String, Object> Epic = prompt(finalToken, om, requestTextForEpic);

        String requestTextForUserstory = promptForUserStory.replace("{epics}", getMessage(Epic));
            Map<String, Object> userStory = prompt(finalToken, om, requestTextForUserstory);

            String res = extractJson(getMessage(userStory));
            if (res == null) {
                throw new NullPointerException("The response 'res' is null and cannot be processed.");
            }
            res = res.replace('\'', '\"');
            return om.readValue(res, MakeUserStoryResponseDto.class);
    }


    @Recover
    public ResponseEntity<?> recoverPromptUserStory(Exception e, OpenAIToken finalToken, ObjectMapper om, List<String> scenario) {
        return ResponseEntity.badRequest().body("{\"error\": \"부적절한 시나리오입니다. 시나리오를 확인해주세요.\"}");
    }



    private static ChatRequestDto.MessageDto createMessageDto(String role, String content) {
        return ChatRequestDto.MessageDto.builder()
                .role(role)
                .content(content)
                .build();
    }

    private ChatRequestDto createAskChatRequestDto(String requestText) {
        ChatRequestDto.MessageDto promptForEpic = createMessageDto("user", requestText);

        return  ChatRequestDto.builder()
                .model(model)
                .messages(Collections.singletonList(promptForEpic))
                .build();
    }

    private String createRequestForEpic(List<String> scenario){
        System.out.println(scenario);
        String replacedString = promptForEpic.replace("{scenario}", "'" + String.join("','", scenario)+ "'");
        return replacedString;
    }

    private void addToken(OpenAIToken finalToken, Map<String, Object> res) {
        OpenAIToken tempToken = getToken(res);
        finalToken.setInToken(finalToken.getInToken() + tempToken.getInToken());
        finalToken.setOutToken(finalToken.getOutToken() + tempToken.getOutToken());
    }
    private Map<String, Object> prompt(OpenAIToken finalToken, ObjectMapper om, String req) throws JsonProcessingException {
        ChatRequestDto chatRequestDtoForEpic = createAskChatRequestDto(req);

        String requestBody = om.writeValueAsString(chatRequestDtoForEpic);

        HttpHeaders headers = chatGPTConfig.httpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                requestEntity,
                String.class);

        String responseBody = response.getBody();

        Map<String, Object> res = parseJsonResponse(om, responseBody);
        addToken(finalToken,res);
        return res;
    }

    private String getMessage( Map<String, Object> res){

        List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");

        Map<String, Object> firstChoice = choices.get(0);

        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

        String content = (String) message.get("content");
        System.out.println(content);
        return content;
    }

    private OpenAIToken getToken(Map<String, Object> res){

        Map<String, Object> usage = (Map<String, Object>) res.get("usage");

        int inToken = (int) usage.get("prompt_tokens");
        int outToken = (int) usage.get("completion_tokens");

        OpenAIToken token =  new OpenAIToken();
        token.setInToken(inToken);
        token.setOutToken(outToken);
        return token;
    }
    private static String extractJson(String text) {
        int startIndex = text.indexOf('{');
        int endIndex = text.lastIndexOf('}');

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return text.substring(startIndex, endIndex + 1);
        }
        return null;
    }


    private static Map<String, Object> parseJsonResponse(ObjectMapper om,String jsonResponse) {
        try {
            return om.readValue(jsonResponse, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}