package com.langchain4j.demo.test.chat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/**
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class ChatModelTest {

    @Value("${deepseek.api-key}")
    private String deepseekApiKey;

    @Resource
    private ChatModel chatModel;


    @Test
    public void testChat() {
        String message = "你谁谁啊？";
        System.out.println("【提问】 " + message);

        String content = chatModel.chat(message);
        System.out.println("【回答】 " + content);
    }

    @Test
    public void testChatAiMessage() {
        String message = "你谁谁啊？";
        System.out.println("【提问】 " + message);

        UserMessage userMessage = UserMessage.from(message);

        SystemMessage systemMessage = SystemMessage.from("请用中文回答。");

        List<ChatMessage> chatMessages = List.of(systemMessage, userMessage);

        AiMessage aiMessage = chatModel.chat(chatMessages).aiMessage();
        String thinking = aiMessage.thinking();
        System.out.println("thinking = " + thinking);
        String text = aiMessage.text();
        System.out.println("text = " + text);
    }


    @Test
    public void testChatRequest() {
        String message = "你谁谁啊？";
        System.out.println("【提问】 " + message);

        ChatRequest chatRequest = ChatRequest.builder()
                .modelName("gpt-5-mini")
                .messages(UserMessage.from(message))
                .build();

        ChatResponse chatResponse = chatModel.chat(chatRequest);

        AiMessage aiMessage = chatResponse.aiMessage();
        String text = aiMessage.text();
        System.out.println("【回答】 " + text);
        System.out.println("【id】 " + chatResponse.id());
        System.out.println("【thinking】 " + aiMessage.thinking());
        System.out.println("【finishReason】 " + chatResponse.finishReason());
        System.out.println("【modelName】 " + chatResponse.modelName());
        System.out.println("【totalTokenCount】" + chatResponse.tokenUsage().totalTokenCount());
        System.out.println("【tokenUsage】 " + chatResponse.tokenUsage());
        System.out.println("【metadata】 " + chatResponse.metadata());

    }

    /**
     * 对话-开启推理
     */
    @Test
    public void testChatReasoning() {
        String message = "你是谁？";
        System.out.println("【提问】 " + message);

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com/v1")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-reasoner")
                .returnThinking(true)
                .build();

        UserMessage userMessage = UserMessage.from(message);
        AiMessage aiMessage = openAiChatModel.chat(userMessage).aiMessage();
        String thinking = aiMessage.thinking();
        System.out.println("【思考】" + thinking);
        String text = aiMessage.text();
        System.out.println("【回答】 " + text);
    }

}
