package com.langchain4j.demo.test.search;

import com.langchain4j.demo.ai.Assistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络搜索 测试
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class WebSearchTest {

    @Value("${searchapi.api-key}")
    private String searchApiKey;

    @Resource
    private ChatModel chatModel;

    @Test
    public void testGoogleSearch() {

        Map<String, Object> optionalParameters = new HashMap<>();
        //optionalParameters.put("gl", "us"); // 搜索国家
        //optionalParameters.put("hl", "en"); // 搜索语言
        //optionalParameters.put("google_domain", "google.com");
        optionalParameters.put("gl", "cn"); // 搜索国家
        optionalParameters.put("hl", "zh-cn"); // 搜索语言
        optionalParameters.put("google_domain", "google.com");


        SearchApiWebSearchEngine searchEngine = SearchApiWebSearchEngine.builder()
                .apiKey(searchApiKey)
                .engine("google")
                .optionalParameters(optionalParameters)
                .build();

        WebSearchTool webTool = WebSearchTool.from(searchEngine);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .tools(webTool)
                .build();

        String message = "深圳今天有什么新闻?";
        System.out.println("【提问】 = " + message);

        String result = assistant.chat(message);

        System.out.println("【回答】 = " + result);
    }

}
