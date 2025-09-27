package com.langchain4j.demo.guardrails;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.InputGuardrailResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 消息检查输入护栏
 *
 * @author dawei
 */
@Slf4j
public class MessageCheckInputGuardrail implements InputGuardrail {

    // 敏感词列表
    public final static List<String> SENSITIVE_WORDS = List.of("干掉", "做掉");

    @Override
    public InputGuardrailResult validate(InputGuardrailRequest params) {
        List<ChatMessage> messages = params.requestParams().chatMemory().messages();

        UserMessage userMessage = params.userMessage();
        String text = userMessage.singleText();
        for (String sensitiveWord : SENSITIVE_WORDS) {
            if (StringUtils.contains(text, sensitiveWord)) {
                String errorMsg = "包含不法词：" + sensitiveWord;
                log.warn(errorMsg);

                // 返回失败信息，后面的输入围栏还会执行，到最后一起抛出错误
                return this.failure(errorMsg);

                // 抛出致命错误，后面的输入围栏不再执行
                //return this.fatal(errorMsg);
            }
        }
        return this.success();
    }

}
