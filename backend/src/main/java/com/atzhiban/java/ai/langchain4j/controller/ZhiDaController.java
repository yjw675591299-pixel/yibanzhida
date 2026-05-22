package com.atzhida.java.ai.langchain4j.controller;

import com.atzhida.java.ai.langchain4j.assistant.ZhiDaAgent;
import com.atzhida.java.ai.langchain4j.bean.ChatForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "鍖讳即鏅虹瓟")
@RestController
@RequestMapping("/zhida")
public class ZhiDaController {

    @Autowired
    private ZhiDaAgent zhiDaAgent;

    @Operation(summary = "瀵硅瘽")
    @PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm) {
        return zhiDaAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }
}

