package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 根据风格与所需内容的契合程度打分
 */
public interface StyleScorer {

    /*@UserMessage("""
            You are a critical reviewer.
            Give a review score between 0.0 and 1.0 for the following
            story based on how well it aligns with the style '{{style}}'.
            Return only the score and nothing else.
            The story is: "{{story}}"
            """)
    @Agent("Scores a story based on how well it aligns with a given style")
    double scoreStyle(@V("story") String story, @V("style") String style);*/

    @UserMessage("""
            你是一个批判性的审稿人。
            给以下内容的评分在 0.0 到 1.0 之间
            故事基于它与样式 "{{style}}" 的一致性。
            只返回分数，不返回其他任何内容。
            故事是："{{story}}"
            """)
    @Agent("根据故事与给定风格的一致性对故事进行评分")
    double scoreStyle(@V("story") String story, @V("style") String style);
}