package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * 电影专家 智能体
 */
public interface MovieExpert {

    /*@UserMessage("""
        You are a great evening planner.
        Propose a list of 3 movies matching the given mood.
        The mood is {mood}.
        Provide a list with the 3 items and nothing else.
        """)
    @Agent
    List<String> findMovie(@V("mood") String mood);*/

    @UserMessage("""
        你是一个很棒的晚间计划者。
        提出 3 部符合给定心情的电影列表。
        心情是 {{mood}}。
        提供包含 3 个项目的列表，仅此而已。
        """)
    @Agent
    List<String> findMovie(@V("mood") String mood);

}