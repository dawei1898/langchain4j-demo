package com.langchain4j.demo.guardrails;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 消息审核输入护栏
 *
 * @author dawei
 */
@Slf4j
public class MessageAuditInputGuardrail  implements InputGuardrail {

    // 敏感词列表
    public final static List<String> SENSITIVE_WORDS = List.of("kill", "fuck");

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText();
        for (String sensitiveWord : SENSITIVE_WORDS) {
            if (StringUtils.contains(text, sensitiveWord)) {
                log.info("回答内容：{}",text);
                String errorMsg = "包含敏感词：" + sensitiveWord;
                log.warn(errorMsg);

                // 返回失败信息，后面的输入围栏还会执行，到最后一起抛出错误
                return this.failure(errorMsg);
            }
        }
        return this.success();
    }

}
