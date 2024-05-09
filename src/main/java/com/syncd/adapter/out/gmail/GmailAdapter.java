package com.syncd.adapter.out.gmail;

import com.syncd.application.port.out.gmail.SendMailPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GmailAdapter implements SendMailPort {

    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴
    private static final String senderEmail= "syncd.official@gmail.com";
    private static int number;  // 랜덤 인증 코드
    @Override
    @Async
    public String sendInviteMail(String email, String hostName, String userName, String projectName) {
        MimeMessage message = createMail(email, hostName, userName, projectName);
        // 실제 메일 전송
        javaMailSender.send(message);

        return "good";
    }

    // 메일 양식 작성
    public MimeMessage createMail(String email, String hostName, String userName, String projectName){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[syncd]"+hostName+"님이 "+projectName+"에 초대하였습니다.");  // 제목 설정
            String body = "";
            body += "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>싱크대 프로젝트 초대</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: auto;\n" +
                    "            padding: 20px;\n" +
                    "            background-color: #fff;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            color: #333;\n" +
                    "        }\n" +
                    "        p {\n" +
                    "            color: #666;\n" +
                    "        }\n" +
                    "        .btn {\n" +
                    "            display: inline-block;\n" +
                    "            padding: 10px 20px;\n" +
                    "            background-color: #007bff;\n" +
                    "            color: #fff;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>싱크대 프로젝트에 초대받았습니다!</h1>\n" +
                    "        <p>"+hostName+"님이 "+userName+"님을 "+projectName+" 프로젝트에 초대되었습니다. 아래 버튼을 클릭하시면 초대에 응하실 수 있습니다</p>\n" +
                    "        <a href=\"https://syncd.i-dear.org\" class=\"btn\">초대 수락</a>\n" +
                    "        <p>위의 버튼이 작동하지 않는 경우 브라우저에 다음 링크를 복사하여 붙여넣을 수도 있습니다:</p>\n" +
                    "        <p>https://syncd.i-dear.org</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n";
            message.setText(body,"UTF-8", "html");
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

}
