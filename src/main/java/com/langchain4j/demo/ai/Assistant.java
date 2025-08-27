package com.langchain4j.demo.ai;

import dev.langchain4j.data.message.AiMessage;
import reactor.core.publisher.Flux;

/**
 * @author dawei
 */
public interface Assistant {

    String chat(String message);

    Flux<String> chatStream(String message);

    Flux<AiMessage> chatStreamReasoning(String message);

}
