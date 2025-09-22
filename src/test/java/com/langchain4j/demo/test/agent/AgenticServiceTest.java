package com.langchain4j.demo.test.agent;

import com.langchain4j.demo.agent.AudienceEditor;
import com.langchain4j.demo.agent.CreativeWriter;
import com.langchain4j.demo.agent.NovelCreator;
import com.langchain4j.demo.agent.StyleEditor;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

/**
 * 智能体服务测试
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class AgenticServiceTest {

    @Resource
    private ChatModel chatModel;

    @Resource
    private OpenAiStreamingChatModel openAiStreamingChatModel;


    @Test
    public void testAgenticService01() {
        CreativeWriter creativeWriter = AgenticServices.agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputName("story")
                .build();

        String message = "如何成为一位优秀程序员";
        System.out.println("【提问】 = " + message);

        String story = creativeWriter.generateStory(message);
        System.out.println("【回答】 = " + story);
    }

    /**
     * 顺序工作流
     */
    @Test
    public void testAgenticService02() {
        String outputName = "story";

        // 作家智能体
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputName(outputName)
                .build();

        // 受众智能体
        AudienceEditor audienceEditor = AgenticServices
                .agentBuilder(AudienceEditor.class)
                .chatModel(chatModel)
                .outputName(outputName)
                .build();

        // 风格智能体
        StyleEditor styleEditor = AgenticServices
                .agentBuilder(StyleEditor.class)
                .chatModel(chatModel)
                .outputName(outputName)
                .build();

        // 创建创作工作流
        UntypedAgent creatorAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(creativeWriter, audienceEditor, styleEditor)
                .outputName(outputName)
                .build();

        // 入参
        Map<String, Object> input = Map.of(
                "topic", "如何成为一名优秀的程序员",
                "style", "实战主义",
                "audience", "年轻人"
        );
        System.out.println("【提问】 = " + input);

        // 调取智能体
        String story = (String)creatorAgent.invoke(input);

        System.out.println("【回答】 = " + story);
    }

    /**
     * 顺序工作流
     */
    @Test
    public void testAgenticService03() {
        String outputName = "story";
        // 作家智能体
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputName(outputName)
                .build();

        // 受众智能体
        AudienceEditor audienceEditor = AgenticServices
                .agentBuilder(AudienceEditor.class)
                .chatModel(chatModel)
                .outputName(outputName)
                .build();

        // 风格智能体
        StyleEditor styleEditor = AgenticServices
                .agentBuilder(StyleEditor.class)
                .chatModel(chatModel)
                .outputName(outputName)
                .build();

        // 小说创作智能体
        NovelCreator novelCreator = AgenticServices
                .sequenceBuilder(NovelCreator.class)
                .subAgents(creativeWriter, audienceEditor, styleEditor)
                .outputName(outputName)
                .build();

        String topic = "如何成为一名优秀的程序员";
        String audience = "实战主义";
        String style = "年轻人";

        System.out.println("topic = " + topic);
        System.out.println("audience = " + audience);
        System.out.println("style = " + style);

        // 调取智能体生成小说
        String story = novelCreator.createNovel(topic , audience, style);

        System.out.println("【回答】 = " + story);
    }


}
