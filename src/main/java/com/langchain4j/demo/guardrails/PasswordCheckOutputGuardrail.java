package com.langchain4j.demo.guardrails;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 密码检查输出护栏
 *
 * @author dawei
 */
@Slf4j
public class PasswordCheckOutputGuardrail implements OutputGuardrail {

    // 敏感词列表
    public final static List<String> SENSITIVE_WORDS = List.of("密码", "password");



    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        for (String sensitiveWord : SENSITIVE_WORDS) {
            if (StringUtils.contains(responseFromLLM.text(), sensitiveWord)) {
                String errorMsg = "回答包含敏感词：" + sensitiveWord;
                log.warn(errorMsg);
                // 返回失败信息，后面的输入围栏还会执行，到最后一起抛出错误
                return this.failure(errorMsg);
            }
        }
        return this.success();
    }



}
