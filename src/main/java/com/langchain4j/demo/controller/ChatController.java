package com.langchain4j.demo.controller;

import com.langchain4j.demo.ai.Assistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 对话服务
 *
 * @author dawei
 */
@Slf4j
@RestController
public class ChatController {


    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;



    @RequestMapping("/chat")
    public Map chat(@RequestParam String message) {
        String result = chatModel.chat(message);
        return Map.of("content", result);
    }

    @RequestMapping(path = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> streamChat(@RequestParam String message) {
        Assistant assistant = AiServices.builder(Assistant.class)
                .streamingChatModel(streamingChatModel)
                .build();
        Flux<String> stringFlux = assistant.chat(message);
        return stringFlux.map(text -> Map.of("content", text));
    }

}
