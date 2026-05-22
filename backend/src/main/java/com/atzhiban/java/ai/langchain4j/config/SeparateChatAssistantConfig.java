package com.atzhida.java.ai.langchain4j.config;

import com.atzhida.java.ai.langchain4j.store.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeparateChatAssistantConfig {

    //娉ㄥ叆鎸佷箙鍖栧璞?
    @Autowired
    private MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
        .id(memoryId)
        .maxMessages(10)
        //.chatMemoryStore(new InMemoryChatMemoryStore)
        .chatMemoryStore(mongoChatMemoryStore)//閰嶇疆鎸佷箙鍖栧璞?
        .build();
    }
}

