package com.langchain4j.demo.controller;

import com.langchain4j.demo.ai.Assistant;
import com.langchain4j.demo.ai.AssistantMemory;
import com.langchain4j.demo.guardrails.MessageCheckInputGuardrail;
import com.langchain4j.demo.guardrails.PasswordCheckOutputGuardrail;
import com.langchain4j.demo.listener.MyAiServiceCompletedListener;
import com.langchain4j.demo.tools.WeatherTool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.*;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 对话服务
 *
 * @author dawei
 */
@Slf4j
@RestController
public class ChatController {

    @Value("${deepseek.api-key}")
    private String deepseekApiKey;


    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    private final ChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(5);

    public static final String DEFAULT_CHAT_ID = UUID.randomUUID().toString();


    /**
     * 对话
     */
    @RequestMapping("/chat")
    public Map<String, String> chat(@RequestParam String message) {
        String result = chatModel.chat(message);

        /*String result = null;
        try {
            Assistant assistant = AiServices.builder(Assistant.class)
                    .chatModel(chatModel)
                    .chatMemory(messageWindowChatMemory)                  // 记忆存储
                    .registerListeners(new MyAiServiceCompletedListener())// 监听对话完成
                    .tools(new WeatherTool())                             // 调用工具
                    .inputGuardrails(new MessageCheckInputGuardrail())    // 输入围栏
                    .outputGuardrails(new PasswordCheckOutputGuardrail()) // 输出围栏
                    .build();
            result = assistant.chat(message);
        } catch (Exception e) {
            return Map.of("content", e.getMessage());
        }*/

        return Map.of("content", result);
    }

    /**
     * 流式对话
     */
    @RequestMapping(path = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> chatStream(@RequestParam String message) {
        Assistant assistant = AiServices.builder(Assistant.class)
                .streamingChatModel(streamingChatModel)
                .chatMemory(messageWindowChatMemory)                  // 记忆存储
                .registerListeners(new MyAiServiceCompletedListener())// 监听对话完成
                .tools(new WeatherTool())                             // 调用工具
                .inputGuardrails(new MessageCheckInputGuardrail())    // 输入围栏
                .outputGuardrails(new PasswordCheckOutputGuardrail()) // 输出围栏
                .build();
        Flux<String> stringFlux = assistant.chatStreamFlux(message);
        return stringFlux.map(text -> Map.of("content", text))
                .concatWith(Flux.just(Map.of("content", "[DONE]")));
    }

    /**
     * 对话 + 推理
     */
    @RequestMapping( "/chat/reasoning")
    public Map<String, String> chatReasoning(@RequestParam String message) {

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com/v1")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-reasoner")
                .returnThinking(true)
                .build();

        UserMessage userMessage = UserMessage.from(message);
        AiMessage aiMessage = openAiChatModel.chat(userMessage).aiMessage();
        String thinking = aiMessage.thinking();
        String text = aiMessage.text();
        return Map.of("reasoningContent", thinking, "content", text);
    }

    /**
     * 流式对话 + 推理
     */
    @RequestMapping( path = "/chat/stream/reasoning", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> chatStreamReasoning(@RequestParam String message) {
         UserMessage userMessage = UserMessage.from(message);

        return Flux.create(sink -> {
            streamingChatModel.chat(List.of(userMessage), new StreamingChatResponseHandler() {
                // 思考内容
                @Override
                public void onPartialThinking(PartialThinking partialThinking) {
                    System.out.println("partialThinking = " + partialThinking.text());
                    sink.next(Map.of("reasoningContent", partialThinking.text()));
                }

                // 回答内容
                @Override
                public void onPartialResponse(String s) {
                    System.out.println("onPartialResponse = " + s);
                    sink.next(Map.of("content", s));
                }

                // 完整的思考和回答内容
                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    System.out.println("chatResponse thinking = " + chatResponse.aiMessage().thinking());
                    System.out.println("chatResponse text = " + chatResponse.aiMessage().text());
                    sink.next(Map.of("content", "[DONE]"));
                    sink.complete();
                }

                @Override
                public void onPartialToolCall(PartialToolCall partialToolCall) {
                    StreamingChatResponseHandler.super.onPartialToolCall(partialToolCall);
                }

                @Override
                public void onCompleteToolCall(CompleteToolCall completeToolCall) {
                    StreamingChatResponseHandler.super.onCompleteToolCall(completeToolCall);
                }

                @Override
                public void onError(Throwable throwable) {
                    sink.error(throwable);
                }
            });
        });
    }

    /**
     * 流式对话 + 推理
     */
    @RequestMapping(path = "/chat/stream/reasoning2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> chatStreamReasoning2(@RequestParam String message,
                                                          @RequestParam(required = false) String chatId) {

        if (StringUtils.isEmpty(chatId)) {
            chatId = DEFAULT_CHAT_ID;
        }

        AssistantMemory assistant = AiServices.builder(AssistantMemory.class)
                .streamingChatModel(streamingChatModel)
                .chatMemoryProvider(chatMemoryProvider) // 记忆存储
                .registerListeners(new MyAiServiceCompletedListener())// 监听对话完成
                //.tools(new WeatherTool())                           // 调用工具 TODO 加了工具后，推理内容为空了
                .inputGuardrails(new MessageCheckInputGuardrail())    // 输入围栏
                //.outputGuardrails(new PasswordCheckOutputGuardrail()) // 输出围栏 TODO 可能影响推理输出
                .build();

        UserMessage userMessage = UserMessage.from(message);
        List<ChatMessage> userMessages = List.of(userMessage);

        TokenStream tokenStream = assistant.streamChat(chatId ,userMessages);

        return Flux.create(sink -> {
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
    }

}
