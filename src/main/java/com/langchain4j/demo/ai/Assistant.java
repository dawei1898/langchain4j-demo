package com.langchain4j.demo.ai;

import reactor.core.publisher.Flux;

/**
 * @author dawei
 */
public interface Assistant {

    Flux<String> chat(String message);

}
