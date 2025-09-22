package com.langchain4j.demo.test.chat;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenChatRequestParameters;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 测试千问文本生成模型
 *
 * @author dawei
 */
@SpringBootTest
@ActiveProfiles("dev")
public class QwenChatModelTest {

    @Value("${langchain4j.community.dashscope.api-key}")
    private String apiKey;

    @Value("${langchain4j.community.dashscope.chat-model.model-name}")
    private String chatModelName;

    /**
     * 纯文本对话
     */
    @Test
    public void testChat() {
        String message = "你是谁啊？";
        System.out.println("【提问】 = " + message);

        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .build();

        UserMessage userMessage = UserMessage.from(message);

        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        System.out.println("【回复】 = " + chatResponse.aiMessage().text());
    }

    /**
     * 对话-开启推理
     */
    @Test
    public void testChatReasoning() {
        String message = "你是谁啊？";
        System.out.println("【提问】 = " + message);

        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .build();

        UserMessage userMessage = UserMessage.from(message);

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .parameters(
                        QwenChatRequestParameters.builder()
                                .enableThinking(true)
                                .build()
                )
                .build();

        ChatResponse chatResponse = qwenChatModel.chat(chatRequest);
        System.out.println("【思考】 = " + chatResponse.aiMessage().thinking());
        System.out.println("【回答】 = " + chatResponse.aiMessage().text());
    }


    /**
     * 对话-开启搜索
     */
    @Test
    public void testChatEnableSearch() {
        String message = "深圳今天的新闻有什么？";
        System.out.println("【提问】 = " + message);

        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .enableSearch(true)
                .build();

        UserMessage userMessage = UserMessage.from(message);

        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        System.out.println("【回复】 = " + chatResponse.aiMessage().text());
    }



}
