package com.langchain4j.demo.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 记忆存储提供者
 *
 * @author dawei
 */
public class MyChatMemoryProvider implements ChatMemoryProvider {

    // 记忆缓存
    private static final Map<String, ChatMemory> CHAT_MEMORY_MAP = new ConcurrentHashMap<>() ;

    @Override
    public ChatMemory get(Object memoryId) {

        ChatMemory result = CHAT_MEMORY_MAP.computeIfAbsent((String) memoryId, key ->
                MessageWindowChatMemory.builder()
                        .id(key)
                        .maxMessages(5)
                        .build()
        );

        return result;
    }
}
