package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 法律专家 智能体
 */
public interface LegalExpert {

    @UserMessage("""
        你是法律专家。
        从法律的角度分析以下用户要求，并提供最佳答案。
        用户请求是 {{request}}。
        """)
    @Agent("法律专家")
    String legal(@V("request") String request);

}