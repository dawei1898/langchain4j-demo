package com.langchain4j.demo.ai;

import com.langchain4j.demo.guardrails.MessageAuditInputGuardrail;
import com.langchain4j.demo.guardrails.MessageCheckInputGuardrail;
import com.langchain4j.demo.guardrails.PasswordCheckOutputGuardrail;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import reactor.core.publisher.Flux;

/**
 * @author dawei
 */
public interface Assistant {

    String chat(String message);

    TokenStream chatStream(@UserMessage String message);

    @InputGuardrails({MessageCheckInputGuardrail.class})
    @OutputGuardrails(value = {PasswordCheckOutputGuardrail.class}, maxRetries = 2)
    TokenStream chatStream2(@UserMessage String message);

    Flux<String> chatStreamFlux(String message);

}
