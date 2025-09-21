package com.langchain4j.demo.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.service.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author dawei
 */
public interface AssistantTest {

    String chat(String message);

    @UserMessage("你是我的好朋友：大雷。用暴躁愤怒的方式回答: {{message}}")
    String chat2(@V("message") String message);

    String chat(@UserMessage ChatMessage... messages);

    String chat(@UserMessage List<ChatMessage> messages);

    String chat(@UserMessage String messages, @UserMessage List<Content> contents);

    String chat(@UserMessage List<ChatMessage> messages, @UserMessage List<Content> contents);

    //String chat(@MemoryId String memoryId, @UserMessage String message);

    @SystemMessage("你是我的好朋友：大壮。用风趣幽默的方式回答。")
    String chatWithSystem(String message);

    @SystemMessage("给定一个国家的名称，用它的首都名称来回答")
    @UserMessage("{{it}}")
    String chatWithSystemMessage(String country);

    @UserMessage("为以下主题的文章生成大纲: {{it}}")
    Result<List<String>> generateOutlineFor(String topic);

    TokenStream chatStream(@UserMessage String message);

    @SystemMessage(fromResource = "prompt/system-prompt.txt")
    Flux<String> chatStreamFlux(String message);

    // TODO 报错
    Flux<AiMessage> chatStreamReasoning(String message);

}
