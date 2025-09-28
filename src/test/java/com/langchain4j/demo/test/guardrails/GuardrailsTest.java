package com.langchain4j.demo.test.guardrails;

import com.langchain4j.demo.ai.AssistantTest;
import com.langchain4j.demo.guardrails.MessageAuditInputGuardrail;
import com.langchain4j.demo.guardrails.MessageCheckInputGuardrail;
import com.langchain4j.demo.guardrails.MoodCheckOutputGuardrail;
import com.langchain4j.demo.guardrails.PasswordCheckOutputGuardrail;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Guardrails 护栏
 *
 * @author dawei
 */
@SpringBootTest
@ActiveProfiles("dev")
public class GuardrailsTest {


    @Resource
    private ChatModel chatModel;


    /**
     * 测试输入护栏
     */
    @Test
    public void testInputGuardrail01() {
        AssistantTest assistant = AiServices
                .builder(AssistantTest.class)
                .chatModel(chatModel)
                .inputGuardrails(
                        new MessageAuditInputGuardrail(),
                        new MessageCheckInputGuardrail()
                ) // 输入护栏
                //.inputGuardrailClasses(MessageCheckInputGuardrail.class , MessageAuditInputGuardrail.class)
                .build();

        String message = "I want to kill you. 我要干掉你。";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        System.out.println("【回答】 = " + content);
    }

    /**
     * 测试输入护栏
     */
    @Test
    public void testInputGuardrail02() {
        AssistantTest assistant = AiServices
                .builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "I want to kill you. 我要干掉你。";
        System.out.println("【提问】 = " + message);

        String content = assistant.chatWithGuardrail(message);
        System.out.println("【回答】 = " + content);
    }

    /**
     * 测试输出护栏-重试
     */
    @Test
    public void testOutputGuardrail01() {
        AssistantTest assistant = AiServices
                .builder(AssistantTest.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .outputGuardrails(new PasswordCheckOutputGuardrail())
                .build();

        String message = "你知道我的手机密码是多少吗？";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        System.out.println("【回答】 = " + content);
    }


    /**
     * 测试输出护栏-重试
     */
    @Test
    public void testOutputGuardrail02() {
        AssistantTest assistant = AiServices
                .builder(AssistantTest.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .outputGuardrails(new MoodCheckOutputGuardrail())
                .build();

        String message = "我今天很伤心，你知道是为什么吗？";
        System.out.println("【提问】 = " + message);

        Result<String> result = assistant.chatWithResult(message);
        System.out.println("【回答】 = " + result.content());
    }

}
