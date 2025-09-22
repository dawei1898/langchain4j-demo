package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

/**
 * 根据给定主题和风格，生成故事小说的智能体
 */
public interface StyledWriter {

    /**
     *
     * @param topic 主题
     * @param style 风格
     * @return 生成故事小说
     */
    @Agent
    String writeStoryWithStyle(@V("topic") String topic, @V("style") String style);
}