package com.langchain4j.demo.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author dawei
 */
public interface AssistantTest {

    @SystemMessage("你是我的好朋友：大壮。用风趣幽默的方式回答。")
    String chat(String message);

    @UserMessage("你是我的好朋友：大雷。用暴躁愤怒的方式回答: {{message}}")
    String chat2(@V("message") String message);

    String chat(@UserMessage ChatMessage... messages);

    String chat(@UserMessage List<ChatMessage> messages);

    String chat(@UserMessage String messages, @UserMessage List<Content> contents);

    String chat(@UserMessage List<ChatMessage> messages, @UserMessage List<Content> contents);

    @SystemMessage("给定一个国家的名称，用它的首都名称来回答")
    @UserMessage("{{it}}")
    String chatWithSystemMessage(String country);

    TokenStream chatStream(@UserMessage String message);

    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Flux<String> chatStreamFlux(String message);

    Flux<AiMessage> chatStreamReasoning(String message);

}
