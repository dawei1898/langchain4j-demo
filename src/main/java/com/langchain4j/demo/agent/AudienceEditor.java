package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 受众编辑 智能体
 *
 * @author dawei
 */
public interface AudienceEditor {

    /*@UserMessage("""
        You are a professional editor.
        Analyze and rewrite the following story to better align
        with the target audience of {{audience}}.
        Return only the story and nothing else.
        The story is "{{story}}".
        """)
    @Agent("Edits a story to better fit a given audience")
    String editStory(@V("story") String story, @V("audience") String audience);*/

    @UserMessage("""
        你是一名专业编辑。
        分析并重写以下故事以更好地对齐
        目标受众为 {{audience}}。
        只返回故事，不返回其他内容。
        故事是 {{story}}。
        """)
    @Agent("编辑故事以更好地适应给定的受众")
    String editStory(@V("story") String story, @V("audience") String audience);
}