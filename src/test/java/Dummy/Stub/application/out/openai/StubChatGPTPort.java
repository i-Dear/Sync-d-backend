package Dummy.Stub.application.out.openai;

import com.syncd.application.port.out.openai.ChatGPTPort;
import com.syncd.dto.MakeUserStoryResponseDto;

import java.util.List;

public class StubChatGPTPort implements ChatGPTPort {
    @Override
    public MakeUserStoryResponseDto makeUserstory(List<String> senarios) {
        return new MakeUserStoryResponseDto();
    }
}
