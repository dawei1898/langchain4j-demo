package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

/**
 * 小说创作 整体
 *
 * @author dawei
 */
public interface NovelCreator {

    @Agent
    String createNovel(@V("topic") String topic, @V("audience") String audience, @V("style") String style);

}