package com.syncd.adapter.out.liveblock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syncd.application.port.out.liveblock.LiveblocksPort;
import com.syncd.dto.LiveblocksTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LiveblockApiAdapter implements LiveblocksPort {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${spring.security.auth.liveBlockSecretKey}")
    private String secretKey;
    @Override
    public LiveblocksTokenDto GetRoomAuthToken(String userId, String name,String img, List<String> projectIds) {
        String url = "https://api.liveblocks.io/v2/authorize-user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+secretKey);

        String jsonBody = createJsonBody(userId,name,img,projectIds);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        return restTemplate.postForObject(url, request, LiveblocksTokenDto.class);
    }

    private String createJsonBody(String userId,String name, String img, List<String> projectIds) {
        Map<String, List<String>> permissions = projectIds.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(), // projectId를 직접 사용
                        Collectors.mapping(role -> "room:write", Collectors.toList())
                ));
        String permissionsJson = null;
        try {
            permissionsJson = objectMapper.writeValueAsString(permissions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String[] randomColorPair = getRandomColorPair();
        String data =  String.format("""
            {
              "userId": "%s",
              "userInfo": {
                "name": "%s",
                "color":["%s","%s"],
                "avatar": "%s"
              },
              "permissions": %s
            }
            """, userId, name,randomColorPair[0],randomColorPair[1], img, permissionsJson);
        return data;
    }

    @Override
    public LiveblocksTokenDto Test(String userId,  String roomId) {
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

        return restTemplate.postForObject(url, request, LiveblocksTokenDto.class);
    }
        // colors 배열
        private static String[][] colors = {
                {"#FF0099", "#FF7A00"},
                {"#002A95", "#00A0D2"},
                {"#6116FF", "#E32DD1"},
                {"#0EC4D1", "#1BCC00"},
                {"#FF00C3", "#FF3333"},
                {"#00C04D", "#00FFF0"},
                {"#5A2BBE", "#C967EC"},
                {"#46BE2B", "#67EC86"},
                {"#F49300", "#FFE600"},
                {"#F42900", "#FF9000"},
                {"#00FF94", "#0094FF"},
                {"#00FF40", "#1500FF"},
                {"#00FFEA", "#BF00FF"},
                {"#FFD600", "#BF00FF"},
                {"#484559", "#282734"},
                {"#881B9A", "#1D051E"},
                {"#FF00F5", "#00FFD1"},
                {"#9A501B", "#1E0505"},
                {"#FF008A", "#FAFF00"},
                {"#22BC09", "#002B1B"},
                {"#FF0000", "#000000"},
                {"#00FFB2", "#000000"},
                {"#0066FF", "#000000"},
                {"#FA00FF", "#000000"},
                {"#00A3FF", "#000000"},
                {"#00FF94", "#000000"},
                {"#AD00FF", "#000000"},
                {"#F07777", "#4E0073"},
                {"#AC77F0", "#003C73"}
        };

        public static String[] getRandomColorPair() {
            Random random = new Random();
            int index = random.nextInt(colors.length);
            return colors[index];
        }

}
