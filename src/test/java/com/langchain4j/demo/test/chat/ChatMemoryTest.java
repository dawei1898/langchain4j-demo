package com.langchain4j.demo.test.chat;


import com.langchain4j.demo.ai.Assistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

/**
 * 对话记忆
 *
 * @author dawei
 */
@SpringBootTest
@ActiveProfiles("dev")
public class ChatMemoryTest {

    @Resource
    private ChatModel chatModel;


    @Test
    public void testChatMemory() {
        String chatId = UUID.randomUUID().toString();

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id(chatId)
                .maxMessages(5)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .build();

        String message1 = "你好！我是小明。我住在火星。";
        System.out.println("【提问】 = " + message1);
        String content1 = assistant.chat(message1);
        System.out.println("【回答】 = " + content1);

        String message2 = "我叫什么名字？";
        System.out.println("【提问】 = " + message2);
        String content2 = assistant.chat(message2);
        System.out.println("【回答】 = " + content2);

        String message3 = "我住在哪里？";
        System.out.println("【提问】 = " + message3);
        String content3 = assistant.chat(message3);
        System.out.println("【回答】 = " + content3);


    }
}
