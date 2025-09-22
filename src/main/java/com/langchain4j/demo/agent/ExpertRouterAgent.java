package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

/**
 * 专家路由器 智能体
 */
public interface ExpertRouterAgent {

    @Agent
    String ask(@V("request") String request);

}