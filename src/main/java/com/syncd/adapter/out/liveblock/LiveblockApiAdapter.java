package com.syncd.adapter.out.liveblock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syncd.application.port.in.GetRoomAuthTokenUsecase;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.application.port.out.liveblock.dto.GetRoomAuthTokenDto;
import com.syncd.application.port.out.liveblock.dto.UserRoleForLiveblocksDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LiveblockApiAdapter implements LiveblocksPort {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${spring.security.auth.liveBlockSecretKey}")
    private String secretKey;
    @Override
    public GetRoomAuthTokenDto GetRoomAuthToken(String userId,  List<UserRoleForLiveblocksDto> roles) {
        String url = "https://api.liveblocks.io/v2/authorize-user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+secretKey);

        String jsonBody = createJsonBody(userId,roles);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
//        System.out.print(request);

        return restTemplate.postForObject(url, request, GetRoomAuthTokenDto.class);
    }

    private String createJsonBody(String userId, List<UserRoleForLiveblocksDto> roles) {
        Map<String, List<String>> permissions = roles.stream()
                .collect(Collectors.groupingBy(
                        UserRoleForLiveblocksDto::projectId,
                        Collectors.mapping(role -> "room:" + role.roomPermission().name().toLowerCase(), Collectors.toList())
                ));

        String permissionsJson = null;
        try {
            permissionsJson = objectMapper.writeValueAsString(permissions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return String.format("""
            {
              "userId": "%s",
              "userInfo": {
                "name": "%s",
                "avatar": "%s"
              },
              "permissions": %s
            }
            """, userId, "정준호", "https://s3.ap-northeast-2.amazonaws.com/elasticbeanstalk-ap-northeast-2-176213403491/media/magazine_img/magazine_327/7ae22985-90e8-44c3-a1d6-ee470ddc9073.jpg", permissionsJson);
    }

    @Override
    public GetRoomAuthTokenDto Test(String userId,  String roomId) {
        String url = "https://api.liveblocks.io/v2/authorize-user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+secretKey);

        String jsonBody = String.format("""
            {
              "userId": "%s",
              "userInfo": {
                "name": "%s",
                "avatar": "%s"
              },
              "permissions": {
                "%s": [
                    "room:write"
                  ]
                }
            }
            """, userId, "정준호", "https://s3.ap-northeast-2.amazonaws.com/elasticbeanstalk-ap-northeast-2-176213403491/media/magazine_img/magazine_327/7ae22985-90e8-44c3-a1d6-ee470ddc9073.jpg",roomId);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        System.out.print(request);
        return restTemplate.postForObject(url, request, GetRoomAuthTokenDto.class);
    }

}
