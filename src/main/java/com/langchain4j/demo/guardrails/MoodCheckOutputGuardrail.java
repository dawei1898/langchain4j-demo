package com.langchain4j.demo.guardrails;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 心情检查输出护栏
 *
 * @author dawei
 */
@Slf4j
public class MoodCheckOutputGuardrail implements OutputGuardrail {

    // 重试词列表
    public final static List<String>  RETRY_WORDS = List.of("伤心", "难过");


    @Override
    public OutputGuardrailResult validate(OutputGuardrailRequest request) {
        ChatResponse chatResponse = request.responseFromLLM();
        String text = chatResponse.aiMessage().text();
        for (String retryWord : RETRY_WORDS) {
            if (StringUtils.contains(text, retryWord)) {
                log.info("回答内容：{}", text);
                String errorMsg = "回答包含需要重试词：" + retryWord;
                log.warn(errorMsg);

                // 让大模型重试，并添加一条提示信息
                String reprompt = "请重新回答，不能包含词语：" + retryWord;
                return this.reprompt(errorMsg, reprompt);
            }
        }
        // 将重试后成功的回答消息返回
        return this.successWith(text);
    }


}
