package com.langchain4j.demo.test.chat;

import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


/**
 * 多模态输入测试
 *
 * @author dawei
 */
@SpringBootTest
@ActiveProfiles("dev")
public class MultimodalityTest {

    public static final String GPT_4O_AUDIO_PREVIEW = "gpt-4o-audio-preview";

    @Resource
    private ChatModel chatModel;

    /**
     * 测试图片输入
     */
    @Test
    public void testImageContent() {
        String message = "图中是什么？";
        System.out.println("【提问】 " + message);

        UserMessage userMessage = UserMessage.from(
                TextContent.from(message),
                ImageContent.from("https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg")
        );

        ChatResponse chatResponse = chatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println("【回答】" + aiMessage.text());
        System.out.println("【totalTokenCount】" + chatResponse.tokenUsage().totalTokenCount());
    }

    /**
     * 测试本地图片输入
     */
    @Test
    public void testLocalImageContent() throws Exception {
        String message = "图中的题目怎么解答？";
        System.out.println("【提问】 " + message);

        // 图片路径
        String currentPath = System.getProperty("user.dir");
        String imagePath = currentPath + "/src/main/resources/temp/image/数学题.jpg";
        // 图片数据
        byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
        String base64Data = Base64.getEncoder().encodeToString(imageBytes);

        UserMessage userMessage = UserMessage.from(
                TextContent.from(message),
                ImageContent.from(base64Data, "image/jpg")
        );

        ChatResponse chatResponse = chatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println("【回答】" + aiMessage.text());
        System.out.println("【totalTokenCount】" + chatResponse.tokenUsage().totalTokenCount());
    }


    /**
     * 测试本地音频输入
     * TODO 报错了
     */
    @Test
    public void testLocalAudioContent() throws Exception {
        String message = "音频里面说了什么？";
        System.out.println("【提问】 " + message);

        // 音频数据
        String currentPath = System.getProperty("user.dir");
        String audioPath = currentPath + "/src/main/resources/temp/audio/welcome.mp3";
        byte[] audioBytes = Files.readAllBytes(Path.of(audioPath));
        String base64Data = Base64.getEncoder().encodeToString(audioBytes);
        AudioContent audioContent = AudioContent.from(base64Data, "audio/mp3");

        //AudioContent audioContent2 = AudioContent.from("https://dashscope.oss-cn-beijing.aliyuncs.com/audios/welcome.mp3");

        UserMessage userMessage = UserMessage.from(
                TextContent.from(message),
                audioContent
        );

        ChatRequest chatRequest = ChatRequest.builder()
                .modelName(GPT_4O_AUDIO_PREVIEW)
                .messages(userMessage)
                .build();

        ChatResponse chatResponse = chatModel.chat(chatRequest);
        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println("【回答】" + aiMessage.text());
        System.out.println("【totalTokenCount】" + chatResponse.tokenUsage().totalTokenCount());
    }

    /**
     * 测试本地PDF文档输入
     *
     * TODO 报错了
     */
    @Test
    public void testPDFContent() throws Exception {
        String message = "文档中有什么内容？";
        System.out.println("【提问】 " + message);

        // 文档路径
        String currentPath = System.getProperty("user.dir");
        String pdfPath = currentPath + "/src/main/resources/temp/docs/百炼系列手机产品介绍.pdf";
        // PDF数据
        byte[] pdfBytes = Files.readAllBytes(Path.of(pdfPath));
        String base64Data = Base64.getEncoder().encodeToString(pdfBytes);
        PdfFileContent pdfFileContent = PdfFileContent.from(base64Data, "application/pdf");

        UserMessage userMessage = UserMessage.from(
                TextContent.from(message),
                pdfFileContent
        );

        ChatResponse chatResponse = chatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println("【回答】" + aiMessage.text());
        System.out.println("【totalTokenCount】" + chatResponse.tokenUsage().totalTokenCount());
    }

}
