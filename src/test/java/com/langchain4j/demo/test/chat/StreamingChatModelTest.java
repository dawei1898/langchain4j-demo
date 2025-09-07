package com.langchain4j.demo.test.chat;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 测试流式对话模型
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class StreamingChatModelTest {

    @Resource
    private OpenAiStreamingChatModel openAiStreamingChatModel;


    @Test
    public void testChat() throws  Exception {
        String message = "你是谁啊？";
        System.out.println("【提问】 " + message);

        CountDownLatch latch = new CountDownLatch(1);

        openAiStreamingChatModel.chat(message, new StreamingChatResponseHandler(){
            @Override
            public void onPartialThinking(PartialThinking partialThinking) {
                System.out.println("【思考中】 = " + partialThinking.text());
            }

            @Override
            public void onPartialResponse(String s) {
                System.out.println("【回答中】 = " + s);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                System.out.println("【思考】 = " + chatResponse.aiMessage().thinking());
                System.out.println("【回答】 = " + chatResponse.aiMessage().text());
                System.out.println("【totalTokenCount】 = " + chatResponse.tokenUsage().totalTokenCount());
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("【错误】 = " + throwable);
            }
        });

        latch.await(60, TimeUnit.SECONDS);
        System.out.println("===== 结束 =====");
    }



}
