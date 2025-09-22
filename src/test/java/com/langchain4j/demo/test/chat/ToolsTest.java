package com.langchain4j.demo.test.chat;

import com.langchain4j.demo.ai.AssistantTest;
import com.langchain4j.demo.tools.MathTool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 工具测试
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class ToolsTest {

    @Resource
    private ChatModel chatModel;

    /**
     * 调用工具
     */
    @Test
    public void testTool01() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .tools(new MathTool())
                .build();

        String message = "What is the square root of 475695037565 ?";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        // 689706.49
        System.out.println("【回答】 = " + content);
    }

}
