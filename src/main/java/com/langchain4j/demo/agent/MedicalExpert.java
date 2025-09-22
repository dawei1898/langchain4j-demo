package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 医疗专家 智能体
 */
public interface MedicalExpert {

    @UserMessage("""
        你是医学专家。
        从医学的角度分析以下用户要求，并提供最佳答案。
        用户请求是 {{request}}。
        """)
    @Agent("医学专家")
    String medical(@V("request") String request);

}