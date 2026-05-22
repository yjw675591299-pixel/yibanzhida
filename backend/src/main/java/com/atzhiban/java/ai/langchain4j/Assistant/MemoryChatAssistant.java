package com.atzhida.java.ai.langchain4j.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

//鏅鸿兘浣?
@AiService(
    wiringMode = EXPLICIT,
    chatModel = "qwenChatModel",
    chatMemory = "chatMemory"
    )
public interface MemoryChatAssistant {

    //@UserMessage("浣犳槸鎴戠殑濂芥湅鍙嬶紝璇风敤涓婃捣璇濆洖绛旈棶棰橈紝骞朵笖娣诲姞涓€浜涜〃鎯呯鍙枫€?{{it}}") //{{it}}琛ㄧず杩欓噷鍞竴鐨勫弬鏁扮殑鍗犱綅绗?
    @UserMessage("浣犳槸鎴戠殑濂芥湅鍙嬶紝璇风敤涓婃捣璇濆洖绛旈棶棰橈紝骞朵笖娣诲姞涓€浜涜〃鎯呯鍙枫€?{{message}}")
    String chat(@V("message") String message);
}

