package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 风格编辑 智能体
 *
 * @author dawei
 */
public interface StyleEditor {

    /*@UserMessage("""
        You are a professional editor.
        Analyze and rewrite the following story to better fit and be more coherent with the {{style}} style.
        Return only the story and nothing else.
        The story is "{{story}}".
        """)
    @Agent("Edits a story to better fit a given style")
    String editStory(@V("story") String story, @V("style") String style);*/

    @UserMessage("""
        你是一名专业编辑。
        分析并重写以下故事，以更好地适应和更连贯 {{style}} 风格。
        只返回故事，不返回其他内容。
        故事是 {{story}}。
        """)
    @Agent("编辑故事以更好地适应给定的风格")
    String editStory(@V("story") String story, @V("style") String style);


}