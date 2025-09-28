package com.langchain4j.demo.listener;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 对话模型监听器
 *
 * @author dawei
 */
@Slf4j
public class MyChatModelListener implements ChatModelListener {


    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        log.info("requestMessages = " + requestContext.chatRequest().messages());
        requestContext.attributes().put("starTime", System.currentTimeMillis());
    }

    /**
     * 计算模型调用时间和token数
     */
    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        ChatRequest chatRequest = responseContext.chatRequest();
        ChatResponse chatResponse = responseContext.chatResponse();
        log.info("responseAiMessage = " + chatResponse.aiMessage());
        Long starTime = (Long) responseContext.attributes().get("starTime");
        log.info("【总耗时】 = " + (System.currentTimeMillis() - starTime));
        log.info("【总Token】 = " + chatResponse.tokenUsage().totalTokenCount());
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {

    }


}
