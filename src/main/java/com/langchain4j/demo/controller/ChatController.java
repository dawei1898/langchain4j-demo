package com.langchain4j.demo.controller;

import com.langchain4j.demo.ai.Assistant;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.*;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 对话服务
 *
 * @author dawei
 */
@Slf4j
@RestController
public class ChatController {

    @Value("${deepseek.api-key}")
    private String deepseekApiKey;


    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;


    @RequestMapping("/chat")
    public Map<String, String> chat(@RequestParam String message) {
        String result = chatModel.chat(message);
        return Map.of("content", result);
    }

    @RequestMapping(path = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> chatStream(@RequestParam String message) {
        Assistant assistant = AiServices.builder(Assistant.class)
                .streamingChatModel(streamingChatModel)
                .build();
        Flux<String> stringFlux = assistant.chatStream(message);
        return stringFlux.map(text -> Map.of("content", text))
                .concatWith(Flux.just(Map.of("content", "[DONE]")));
    }

    @RequestMapping( "/chat/reasoning")
    public Map<String, String> chatReasoning(@RequestParam String message) {

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com/v1")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-reasoner")
                .returnThinking(true)
                .build();

        UserMessage userMessage = UserMessage.from(message);
        AiMessage aiMessage = openAiChatModel.chat(userMessage).aiMessage();
        String thinking = aiMessage.thinking();
        String text = aiMessage.text();
        return Map.of("reasoningContent", thinking, "content", text);
    }

    @RequestMapping( path = "/chat/stream/reasoning", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> chatStreamReasoning(@RequestParam String message) {
         UserMessage userMessage = UserMessage.from(message);

        return Flux.create(sink -> {
            streamingChatModel.chat(List.of(userMessage), new StreamingChatResponseHandler() {
                // 思考内容
                @Override
                public void onPartialThinking(PartialThinking partialThinking) {
                    System.out.println("partialThinking = " + partialThinking.text());
                    sink.next(Map.of("reasoningContent", partialThinking.text()));
                }

                // 回答内容
                @Override
                public void onPartialResponse(String s) {
                    System.out.println("onPartialResponse = " + s);
                    sink.next(Map.of("content", s));
                }

                // 完整的思考和回答内容
                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    System.out.println("chatResponse thinking = " + chatResponse.aiMessage().thinking());
                    System.out.println("chatResponse text = " + chatResponse.aiMessage().text());
                    sink.next(Map.of("content", "[DONE]"));
                    sink.complete();
                }

                @Override
                public void onPartialToolCall(PartialToolCall partialToolCall) {
                    StreamingChatResponseHandler.super.onPartialToolCall(partialToolCall);
                }

                @Override
                public void onCompleteToolCall(CompleteToolCall completeToolCall) {
                    StreamingChatResponseHandler.super.onCompleteToolCall(completeToolCall);
                }

                @Override
                public void onError(Throwable throwable) {
                    sink.error(throwable);
                }
            });
        });
    }

}
