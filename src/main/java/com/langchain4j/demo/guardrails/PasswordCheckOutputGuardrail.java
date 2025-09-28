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



    /**
     * 验证AI消息响应是否包含敏感词
     *
     * @param responseFromLLM 来自大语言模型的AI消息响应
     * @return OutputGuardrailResult 验证结果，包含成功、失败或重新提示的信息
     */
    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String thinking = responseFromLLM.thinking();
        String text = responseFromLLM.text();
        for (String sensitiveWord : SENSITIVE_WORDS) {
            if (StringUtils.contains(text, sensitiveWord)) {
                String errorMsg = "回答包含敏感词：" + sensitiveWord;
                log.warn(errorMsg);
                // 返回失败信息，后面的输入围栏还会执行，到最后一起抛出错误
                // return this.failure(errorMsg);

                // 让大模型重试，并添加一条提示信息
                String reprompt = "将 ‘密码’ 替换为 ‘psw’ 再回答：" + sensitiveWord;
                return this.reprompt(errorMsg, reprompt);
            }
        }
        // 将重试后成功的回答消息返回
        if (StringUtils.isNotEmpty(thinking)) {
            return this.successWith(thinking);
        } else {
            return this.successWith(text);
        }
    }



}
