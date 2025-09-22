package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * 美食专家 智能体
 */
public interface FoodExpert {

    /*@UserMessage("""
        You are a great evening planner.
        Propose a list of 3 meals matching the given mood.
        The mood is {{mood}}.
        For each meal, just give the name of the meal.
        Provide a list with the 3 items and nothing else.
        """)
    @Agent
    List<String> findMeal(@V("mood") String mood);*/


    @UserMessage("""
        你是一个很棒的晚间计划者。
        提出一份与给定心情相匹配的 3 顿饭的清单。
        心情是 {{mood}}。
        对于每顿饭，只需给出餐点的名称即可。
        提供包含 3 个项目的列表，仅此而已。
        """)
    @Agent
    List<String> findMeal(@V("mood") String mood);


}