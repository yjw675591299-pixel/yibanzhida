package com.atzhida.java.ai.langchain4j.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTools {

    @Tool(name = "鍔犳硶", value = "杩斿洖涓や釜鍙傛暟鐩稿姞涔嬪拰")
    double sum(
            @ToolMemoryId int memoryId,
            @P(value="鍔犳暟1", required = true) double a,
            @P(value="鍔犳暟2", required = true) double b
    ) {
        System.out.println("璋冪敤鍔犳硶杩愮畻");
        return a + b;
    }

    @Tool(name = "骞虫柟鏍?, value = "杩斿洖缁欏畾鍙傛暟鐨勫钩鏂规牴")
    double squareRoot(@ToolMemoryId int memoryId, double x) {
        System.out.println("璋冪敤骞虫柟鏍硅繍绠?);
        return Math.sqrt(x);
    }
}

