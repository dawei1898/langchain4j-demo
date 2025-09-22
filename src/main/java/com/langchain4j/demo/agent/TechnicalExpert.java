package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 科技专家
 */
public interface TechnicalExpert {

    @UserMessage("""
        你是科技专家。
        从科技的角度分析以下用户要求，并提供最佳答案。
        用户请求是 {{request}}。
        """)
    @Agent("科技专家")
    String technical(@V("request") String request);

}