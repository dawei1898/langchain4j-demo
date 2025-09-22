package com.langchain4j.demo.ai;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * @author dawei
 */
public interface Assistant {

    String chat(String message);

    TokenStream chatStream(@UserMessage String message);

    Flux<String> chatStreamFlux(String message);

}
