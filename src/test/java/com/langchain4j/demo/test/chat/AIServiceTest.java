package com.langchain4j.demo.test.chat;


import com.langchain4j.demo.ai.AssistantTest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 测试 AIService
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class AIServiceTest {

    public static final String IMAGE_URL = "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg";

    @Resource
    private ChatModel chatModel;

    @Resource
    private OpenAiStreamingChatModel openAiStreamingChatModel;


    /**
     * 对话-同步
     */
    @Test
    public void testAIService() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);

        // @SystemMessage("你是我的好朋友：大壮。用风趣幽默的方式回答。")
        String content = assistant.chat(message);
        System.out.println("【回答】 = " + content);
    }


    /**
     * 对话-同步
     */
    @Test
    public void testAIService2() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);

        // @UserMessage("你是我的好朋友：大雷。用暴躁愤怒的方式回答: {{message}}")
        String content = assistant.chat2(message);
        System.out.println("【回答】 = " + content);
    }

    /**
     * 对话-同步 带图片
     */
    @Test
    public void testAIService3() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "图片中是什么内容？";
        System.out.println("【提问】 = " + message);

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(UserMessage.from(message));

        ImageContent imageContent = ImageContent.from(IMAGE_URL);
        chatMessages.add(UserMessage.from(imageContent));

        String content = assistant.chat(chatMessages);
        System.out.println("【回答】 = " + content);
    }

    /**
     * 对话-同步 带图片
     */
    @Test
    public void testAIService4() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "图片中是什么内容？";
        System.out.println("【提问】 = " + message);
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(UserMessage.from(message));

        List<Content> contents = new ArrayList<>();
        ImageContent imageContent = ImageContent.from(IMAGE_URL);
        contents.add(imageContent);

        String content = assistant.chat(chatMessages, contents);
        System.out.println("【回答】 = " + content);
    }


    /**
     * 对话-同步 带系统提示词
     */
    @Test
    public void testAIService5() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "中国";
        System.out.println("【提问】 = " + message);

        // @SystemMessage("给定一个国家的名称，用它的首都名称来回答")
        String content = assistant.chatWithSystemMessage(message);
        System.out.println("【回答】 = " + content);
    }


    /**
     * 对话-流式
     */
    @Test
    public void testChatStream01() throws Exception {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .streamingChatModel(openAiStreamingChatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        TokenStream tokenStream = assistant.chatStream(message);

        tokenStream.onPartialThinking(thinking -> {
                    System.out.println("【推理】 = " + thinking.text());
                })
                .onPartialResponse(partialResponse -> {
                    System.out.println("【回答】 = " + partialResponse);
                })
                .onCompleteResponse(chatResponse -> {
                    System.out.println("【完整推理】 = " + chatResponse.aiMessage().thinking());
                    System.out.println("【完整回答】 = " + chatResponse.aiMessage().text());
                    futureResponse.complete(chatResponse);
                })
                .onError((Throwable error) -> {
                    System.out.println("【错误】 = " + error.getMessage());
                    futureResponse.completeExceptionally(error);
                })
                .start();

        futureResponse.join();

        //TimeUnit.SECONDS.sleep(10);
        System.out.println("【结束】");
    }

    /**
     * 对话-流式
     */
    @Test
    public void testChatStream02() throws Exception {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .streamingChatModel(openAiStreamingChatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);


        TokenStream tokenStream = assistant.chatStream(message);

        Flux<Map<String, String>> mapFlux = Flux.create(sink -> {
            tokenStream.onPartialThinking(thinking -> {
                        System.out.println("【推理】 = " + thinking.text());
                        sink.next(Map.of("reasoningContent", thinking.text()));
                    })
                    .onPartialResponse(partialResponse -> {
                        System.out.println("【回答】 = " + partialResponse);
                        sink.next(Map.of("content", partialResponse));
                    })
                    .onCompleteResponse(chatResponse -> {

                        System.out.println("【完整推理】 = " + chatResponse.aiMessage().thinking());
                        System.out.println("【完整回答】 = " + chatResponse.aiMessage().text());
                        sink.next(Map.of("content", "[DONE]"));
                        sink.complete();
                    })
                    .onError((Throwable error) -> {
                        System.out.println("【错误】 = " + error.getMessage());
                        sink.error(error);
                    })
                    .start();
        });

        mapFlux.subscribe(map -> {
            System.out.println("【内容】 = " + map);
        });

        TimeUnit.SECONDS.sleep(20);
        System.out.println("【结束】");
    }

    /**
     * 对话-流式 Flux
     */
    @Test
    public void testChatStreamFlux() throws Exception {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .streamingChatModel(openAiStreamingChatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);

        Flux<String> stringFlux = assistant.chatStreamFlux(message);

        stringFlux.subscribe(text -> {
            System.out.println("【回答】 = " + text);
        });

        TimeUnit.SECONDS.sleep(10);
        System.out.println("【结束】");
    }

    /**
     * 对话-流式 Flux
     *
     * TODO 报错了！！！ Flux 泛型只能是 String
     */
    @Test
    public void testChatStreamFluxAIMessage() throws Exception {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .streamingChatModel(openAiStreamingChatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);

        // TODO 报错了！！！ Flux 泛型只能是 String
        Flux<AiMessage> aiMessageFlux = assistant.chatStreamReasoning(message);

        aiMessageFlux.subscribe(aiMessage -> {
            System.out.println("【推理】 = " + aiMessage.thinking());
            System.out.println("【回答】 = " + aiMessage.text());
        });

        TimeUnit.SECONDS.sleep(10);
        System.out.println("【结束】");
    }

}
