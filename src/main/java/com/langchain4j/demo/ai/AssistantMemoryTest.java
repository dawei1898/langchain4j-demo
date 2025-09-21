package com.langchain4j.demo.ai;


import dev.langchain4j.service.*;


/**
 * @author dawei
 */
public interface AssistantMemoryTest {


    String chat(@MemoryId String memoryId, @UserMessage String message);

}
