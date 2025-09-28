package com.langchain4j.demo.ai;


import com.langchain4j.demo.guardrails.MessageCheckInputGuardrail;
import com.langchain4j.demo.guardrails.PasswordCheckOutputGuardrail;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;

import java.util.List;


/**
 * @author dawei
 */
public interface AssistantMemory {

    //@InputGuardrails({MessageCheckInputGuardrail.class})
    //@OutputGuardrails(value = {PasswordCheckOutputGuardrail.class}, maxRetries = 2)
    TokenStream streamChat(@MemoryId String memoryId, @UserMessage List<ChatMessage> messages);

}
