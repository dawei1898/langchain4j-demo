package com.langchain4j.demo.test.agent;

import com.alibaba.fastjson2.JSON;
import com.langchain4j.demo.agent.*;
import com.langchain4j.demo.enums.RequestCategory;
import com.langchain4j.demo.model.EveningPlan;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

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


    /**
     * 循环工作流
     */
    @Test
    public void testLoopWorkflow() {
        // 作家智能体
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputName("story")
                .build();

        // 风格评分智能体
        StyleScorer styleScorer = AgenticServices
                .agentBuilder(StyleScorer.class)
                .chatModel(chatModel)
                .outputName("score")
                .build();

        // 风格编辑智能体
        StyleEditor styleEditor = AgenticServices
                .agentBuilder(StyleEditor.class)
                .chatModel(chatModel)
                .outputName("story")
                .build();

        // 风格评分编辑智能体
        UntypedAgent styleReviewLoop = AgenticServices
                .loopBuilder() // 循环执行
                .subAgents(styleScorer, styleEditor)
                .maxIterations( 3) // 最大循环次数
                .exitCondition(agenticScope -> agenticScope.readState("score", 0.0) >= 0.8) // 循环结束条件
                .build();


        // 根据主题、风格创作智能体
        StyledWriter styledWriter = AgenticServices
                .sequenceBuilder(StyledWriter.class)
                .subAgents(creativeWriter, styleReviewLoop)
                .outputName("story")
                .build();


        String topic = "如何成为一名优秀的程序员";
        String style = "实战主义";
        System.out.println("topic = " + topic);
        System.out.println("style = " + style);

        String story = styledWriter.writeStoryWithStyle(topic, style);
        System.out.println("【回答】 = " + story);
    }


    /**
     * 并行工作流
     */
    @Test
    public void testParallelWorkflow() {
        // 美食智能体
        FoodExpert foodExpert = AgenticServices
                .agentBuilder(FoodExpert.class)
                .chatModel(chatModel)
                .outputName("meals")
                .build();

        // 电影智能体
        MovieExpert movieExpert = AgenticServices
                .agentBuilder(MovieExpert.class)
                .chatModel(chatModel)
                .outputName("movies")
                .build();


        // 美食电影计划智能体
        EveningPlannerAgent eveningPlannerAgent = AgenticServices
                .parallelBuilder(EveningPlannerAgent.class)
                .subAgents(foodExpert, movieExpert)
                .executor(Executors.newFixedThreadPool(2))
                .outputName("plans")
                .output(agenticScope -> {
                    List<String> meals = agenticScope.readState("meals", new ArrayList<>());
                    List<String> movies = agenticScope.readState("movies", new ArrayList<>());
                    // 合并 meals 和 movies
                    List<EveningPlan> plans = new ArrayList<>();
                    for (int i = 0; i < meals.size(); i++) {
                        if (i < movies.size()) {
                            plans.add(new EveningPlan(meals.get(i), movies.get(i)));
                        }
                    }
                    return plans;
                })
                .build();

        String mood = "难过";
        System.out.println("mood = " + mood);
        // 并行调用美食、电影计划智能体
        List<EveningPlan> plans = eveningPlannerAgent.plan(mood);
        System.out.println("【回答】 = " + JSON.toJSONString(plans));

    }

    /**
     * 条件工作流
     */
    @Test
    public void testConditionWorkflow() {
        // 问题路由智能体
        CategoryRouter categoryRouter = AgenticServices
                .agentBuilder(CategoryRouter.class)
                .chatModel(chatModel)
                .outputName("category")
                .build();

        // 法律专家智能体
        LegalExpert legalExpert = AgenticServices
                .agentBuilder(LegalExpert.class)
                .chatModel(chatModel)
                .outputName("response")
                .build();

        // 科技专家智能体
        TechnicalExpert technicalExpert = AgenticServices
                .agentBuilder(TechnicalExpert.class)
                .chatModel(chatModel)
                .outputName("response")
                .build();

        // 医疗专家智能体
        MedicalExpert medicalExpert = AgenticServices
                .agentBuilder(MedicalExpert.class)
                .chatModel(chatModel)
                .outputName("response")
                .build();

        // 条件判断
        UntypedAgent expertsAgent = AgenticServices
                .conditionalBuilder()
                .subAgents(agenticScope -> agenticScope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.LEGAL, legalExpert)
                .subAgents(agenticScope -> agenticScope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.TECHNICAL, technicalExpert)
                .subAgents(agenticScope -> agenticScope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.MEDICAL, medicalExpert)
                .build();

        ExpertRouterAgent expertRouterAgent = AgenticServices
                .sequenceBuilder(ExpertRouterAgent.class)
                .subAgents(categoryRouter, expertsAgent)
                .outputName("response")
                .build();

        String request = "前公司工资没结清，请问我如何追回来？";
        System.out.println("request = " + request);
        String response = expertRouterAgent.ask(request);
        System.out.println("【回答】 = " + response);

    }

}
