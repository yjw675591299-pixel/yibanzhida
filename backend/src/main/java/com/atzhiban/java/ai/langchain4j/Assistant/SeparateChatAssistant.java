package com.atzhida.java.ai.langchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
    //    chatMemory = "chatMemory",
        chatMemoryProvider = "chatMemoryProvider",
        chatModel = "qwenChatModel",
        tools = "calculatorTools" //閰嶇疆tools

)
public interface SeparateChatAssistant {

    /**
    * 鍒嗙鑱婂ぉ璁板綍
    * @param memoryId 鑱婂ぉid
    * @param userMessage 鐢ㄦ埛娑堟伅
    * @return
    */
    //@SystemMessage("浣犳槸鎴戠殑濂芥湅鍙嬶紝璇风敤涓滃寳璇濆洖绛旈棶棰樸€備粖澶╂槸{{current_date}}")//绯荤粺娑堟伅鎻愮ず璇?
    @SystemMessage(fromResource = "my-prompt-template.txt")//绯荤粺娑堟伅鎻愮ず璇?
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);

    @UserMessage("浣犳槸鎴戠殑濂芥湅鍙嬶紝璇风敤绮よ鍥炵瓟闂銆倇{message}}")
    String chat2(@MemoryId int memoryId, @V("message") String userMessage);

    @SystemMessage(fromResource = "my-prompt-template3.txt")
    String chat3(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age
    );




}

