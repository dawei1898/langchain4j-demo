package com.langchain4j.demo.listener;

import com.alibaba.fastjson2.JSON;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.observability.api.listener.AiServiceCompletedListener;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * AiService 调用完成监听器
 *
 * @author dawei
 */
@Slf4j
public class MyAiServiceCompletedListener implements AiServiceCompletedListener {

    @Override
    public void onEvent(AiServiceCompletedEvent event) {
        log.info("模型调用完成：{}", event.toString());
        InvocationContext invocationContext = event.invocationContext();
        UUID invocationId = invocationContext.invocationId();
        String aiServiceInterfaceName = invocationContext.interfaceName();
        String aiServiceMethodName = invocationContext.methodName();
        List<Object> aiServiceMethodArgs = invocationContext.methodArguments();
        Object chatMemoryId = invocationContext.chatMemoryId();
        Instant eventTimestamp = invocationContext.timestamp();
    }
}
