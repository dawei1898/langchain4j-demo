package com.langchain4j.demo.test.chat;

import dev.langchain4j.community.model.dashscope.QwenChatRequestParameters;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 测试千问文本生成模型
 *
 * @author dawei
 */
@SpringBootTest
@ActiveProfiles("dev")
public class QwenStreamChatModelTest {

    @Value("${langchain4j.community.dashscope.api-key}")
    private String apiKey;

    @Value("${langchain4j.community.dashscope.chat-model.model-name}")
    private String chatModelName;

    /**
     * 流式对话
     */
    @Test
    public void testChat() throws Exception {
        String message = "你是谁啊？深圳今天天气怎么样？";
        System.out.println("【提问】 = " + message);

        // 监听器
        List<ChatModelListener> listeners = List.of(new ChatModelListener() {
            @Override
            public void onRequest(ChatModelRequestContext requestContext) {
                System.out.println("requestMessages = " + requestContext.chatRequest().messages());
                requestContext.attributes().put("starTime", System.currentTimeMillis());
            }

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                ChatRequest chatRequest = responseContext.chatRequest();
                ChatResponse chatResponse = responseContext.chatResponse();
                System.out.println("responseAiMessage = " + chatResponse.aiMessage());
                Long starTime = (Long) responseContext.attributes().get("starTime");
                System.out.println("【总耗时】 = " + (System.currentTimeMillis() - starTime));
                System.out.println("【总Token】 = " + chatResponse.tokenUsage().totalTokenCount());
            }
        });

        // 模型
        QwenStreamingChatModel qbaStreamingChatModel = QwenStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModelName)
                .listeners(listeners)
                .build();

        UserMessage userMessage = UserMessage.from(message);

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .parameters(
                        QwenChatRequestParameters.builder()
                                .enableThinking(true) // 推理
                                .enableSearch(true) // 搜索
                                .build()
                )
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        qbaStreamingChatModel.chat(chatRequest, new StreamingChatResponseHandler() {
            @Override
            public void onPartialThinking(PartialThinking partialThinking) {
                System.out.println("【思考中】 = " + partialThinking.text());
            }

            @Override
            public void onPartialResponse(String s) {
                System.out.println("【回复中】 = " + s);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                System.out.println("【总思考】 = " + chatResponse.aiMessage().thinking());
                System.out.println("【总回复】 = " + chatResponse.aiMessage().text());
                System.out.println("【totalTokenCount】 = " + chatResponse.tokenUsage().totalTokenCount());
                countDownLatch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        countDownLatch.await(60, TimeUnit.SECONDS);
        System.out.println("==== 结束 =====");

    }


}
