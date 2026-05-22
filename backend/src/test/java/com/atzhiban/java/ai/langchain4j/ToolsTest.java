package com.atzhida.java.ai.langchain4j;

import com.atzhida.java.ai.langchain4j.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToolsTest {
    @Autowired
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testCalculatorTools() {
    String answer = separateChatAssistant.chat(1, "1+2绛変簬鍑狅紝475695037565鐨勫钩鏂规牴鏄灏戯紵");
    //绛旀锛?锛?89706.4865
    System.out.println(answer);
    }
}

