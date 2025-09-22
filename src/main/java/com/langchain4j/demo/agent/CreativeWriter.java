package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 作家 智能体
 *
 * @author dawei
 */
public interface CreativeWriter {

    /*@UserMessage("""
            You are a creative writer.
            Generate a draft of a story no more than
            3 sentences long around the given topic.
            Return only the story and nothing else.
            The topic is {{topic}}.
            """)
    @Agent("Generates a story based on the given topic")
    String generateStory(@V("topic") String topic);*/

    @UserMessage("""
            你是一位富有创造力的作家。
            围绕给定主题生成不超过 3 句话的故事草稿。
            只返回故事，不返回其他内容。
            主题是 {{topic}}.
            """)
    @Agent("根据给定主题生成故事")
    String generateStory(@V("topic") String topic);

}
