package com.atzhida.java.ai.langchain4j;

import com.atzhida.java.ai.langchain4j.assistant.Assistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTest {
    @Autowired
    private QwenChatModel qwenChatModel;
    @Test
    public void testChat() {
    //鍒涘缓AIService
    Assistant assistant = AiServices.create(Assistant.class, qwenChatModel);
    //璋冪敤service鐨勬帴鍙?
    String answer = assistant.chat("Hello");
    System.out.println(answer);
    }



    @Autowired
    private Assistant assistant;
    @Test
    public void testAssistant() {
        String answer = assistant.chat("浣犳槸璋侊紵");
        System.out.println(answer);
    }

}

