package com.langchain4j.demo.test.chat;

import com.langchain4j.demo.ai.AssistantTest;
import com.langchain4j.demo.tools.CalculatorTool;
import com.langchain4j.demo.tools.WeatherTool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
                .tools(new CalculatorTool())
                .build();

        String message = "What is the square root of 475695037565 ?";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        // 689706.49
        System.out.println("【回答】 = " + content);
    }


    /**
     * 调用工具
     */
    @Test
    public void testTool02() {
        String message = "深圳的天气怎么样 ?";
        System.out.println("【提问】 = " + message);

        // 定义天气工具
        List<ToolSpecification> toolSpecifications
                = ToolSpecifications.toolSpecificationsFrom(WeatherTool.class);

        // 构建提问请求
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(UserMessage.from(message))
                .toolSpecifications(toolSpecifications)
                .build();
        // 调用模型
        ChatResponse chatResponse = chatModel.chat(chatRequest);

        System.out.println("【回答】 = " +  chatResponse.aiMessage().text());
    }

    /**
     * 调用工具
     */
    @Test
    public void testTool03() {
        AssistantTest assistant = AiServices.builder(AssistantTest.class)
                .chatModel(chatModel)
                .tools(new WeatherTool())
                .build();

        String message = "深圳今天天气怎么样?";
        System.out.println("【提问】 = " + message);

        String content = assistant.chat(message);
        //
        System.out.println("【回答】 = " + content);
    }

}
