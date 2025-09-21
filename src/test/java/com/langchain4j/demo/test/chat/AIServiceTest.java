package com.langchain4j.demo.test.chat;


import com.langchain4j.demo.ai.AssistantMemoryTest;
import com.langchain4j.demo.ai.AssistantTest;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.message.*;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
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

import static dev.langchain4j.model.openai.OpenAiModerationModelName.TEXT_MODERATION_LATEST;

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

    @Resource
    private OpenAiModerationModel moderationModel;


    /**
     * 对话-同步
     */
    @Test
    public void testChat01() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "你是谁？";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        System.out.println("【回答】 = " + content);
    }


    /**
     * 对话-同步
     */
    @Test
    public void testChat02() {
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
    public void testChat03() {
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
    public void testChat04() {
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
    public void testChat05() {
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
     * 对话-同步 自定义返回类型
     */
    @Test
    public void testReturnType() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .build();

        String message = "java";
        System.out.println("【提问】 = " + message);

        // @UserMessage("为以下主题的文章生成大纲: {{it}}")
        Result<List<String>> result = assistant.generateOutlineFor(message);

        for (String content : result.content()) {
            System.out.println("【回答】 = " + content);
        }
        System.out.println("result.finalResponse() = " + result.finalResponse());

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


    /**
     * 对话-带记忆
     */
    @Test
    public void testChatModel() throws Exception {

        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory.withMaxMessages(5);
            }
        };

        AssistantMemoryTest assistant = AiServices.builder(AssistantMemoryTest.class)
                .chatModel(chatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();

        String chatId = "1";
        String chatId2 = "2";

        String message = "我住在深圳湾一号，张三是我朋友，李四是我表弟。深圳天气怎么样？";
        System.out.println("【提问1】 = " + message);

        String content = assistant.chat(chatId, message);
        System.out.println("【回答1】 = " + content);

        String message2 = "我住在哪里？";
        System.out.println("【提问2】 = " + message2);
        String content2 = assistant.chat(chatId2, message2);
        System.out.println("【回答2】 = " + content2);

        String message3 = "我表弟是谁？";
        System.out.println("【提问3】 = " + message3);
        String content3 = assistant.chat(chatId, message3);
        System.out.println("【回答3】 = " + content3);

    }

    // 工具类
    static class Tools {

        // 加法
        @Tool
        int add(int a, int b) {
            return a + b;
        }

        // 减法
        @Tool
        int subtract(int a, int b) {
            return a - b;
        }

        // 乘法
        @Tool
        int multiply(int a, int b) {
            return a * b;
        }


        // 除法
        @Tool
        int divide(int a, int b) {
            return a / b;
        }
    }

    /**
     * 工具调用
     */
    @Test
    public void testTools01() throws Exception {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .tools(new Tools())
                .build();

        String message = "帮我算一下 1 + 3 * 2 等于多少？";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        System.out.println("【回答】 = " + content);

    }


    /**
     * 检索增强生成
     * TODO 报错
     */
    @Test
    public void testContentRetriever() {
        // 读取文档
        String classPath = "temp/docs/最伟大的人.txt";
        Document document = ClassPathDocumentLoader.loadDocument(classPath);

        // 创建内存向量库
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        // 将文档添加到向量库中 TODO 报错
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
        // 创建内容检索器
        ContentRetriever contentRetriever
                = EmbeddingStoreContentRetriever.from(embeddingStore);


        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();

        String message = "谁是最伟大的人？";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        System.out.println("【回答】 = " + content);


    }


    /**
     * 自动内容审核
     */
    @Test
    public void testAutoContentModeration() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .moderationModel(moderationModel)
                .build();

        String message = "我要 kill 了你!!!";
        System.out.println("【提问】 = " + message);

        try {
            String content = assistant.chat(message);
            System.out.println("【回答】 = " + content);
        } catch (Exception e) {
            System.out.println("检测到内容审核报错 = " + e);
        }
    }

}
