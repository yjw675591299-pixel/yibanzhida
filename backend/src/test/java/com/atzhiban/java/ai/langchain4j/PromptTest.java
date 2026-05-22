package com.atzhida.java.ai.langchain4j;

import com.atzhida.java.ai.langchain4j.assistant.MemoryChatAssistant;
import com.atzhida.java.ai.langchain4j.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {
    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testSystemMessage() {
        String answer = separateChatAssistant.chat(3,"浣犳槸璋侊紵");
        System.out.println(answer);
    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testUserMessage() {
        String answer1 = memoryChatAssistant.chat("鎴戞槸鐜幆");
        System.out.println(answer1);

        String answer2 = memoryChatAssistant.chat("鎴?8浜?);
        System.out.println(answer2);

        String answer3 = memoryChatAssistant.chat("浣犵煡閬撴垜鏄皝鍚楋紵澶氬ぇ浜?);
        System.out.println(answer3);
    }


    @Test
    public void testV() {
        String answer1 = separateChatAssistant.chat2(10, "鎴戞槸鐜幆");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat2(10, "鎴戞槸璋?);
        System.out.println(answer2);
    }


    @Test
    public void testUserInfo() {
        String answer = separateChatAssistant.chat3(1, "鎴戞槸璋侊紝鎴戝澶т簡", "缈犺姳", 18);
        System.out.println(answer);
    }


}

