package com.langchain4j.demo.test.image;

import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 测试万象生图模型
 *
 * @author dawei
 */
@SpringBootTest
@ActiveProfiles("dev")
public class WanxImageModelTest {

    @Value("${langchain4j.community.dashscope.api-key}")
    private String apiKey;

    @Value("${langchain4j.community.dashscope.chat-model.model-name}")
    private String chatModelName;

    @Value("${langchain4j.community.dashscope.image-model.model-name}")
    private String imageModelName;

    @Test
    public void testImageModel() {
        String message = "一架隐身战斗机从天空飞过";
        WanxImageModel imageModel = WanxImageModel.builder()
                .apiKey(apiKey)
                .modelName(imageModelName)
                .build();
        Response<Image> imageResponse = imageModel.generate(message);
        Image image = imageResponse.content();
        System.out.println("【图片 URL】" + image.url());
        // https://dashscope-result-wlcb-acdr-1.oss-cn-wulanchabu-acdr-1.aliyuncs.com/1d/aa/20250906/e731159d/dd02736e-8e79-4449-bc66-973c306d7fb33251872226.png?Expires=1757254038&OSSAccessKeyId=LTAI5tKPD3TMqf2Lna1fASuh&Signature=7jJFDsQrCHWC8gHIqWNwLABRv0U%3D
        System.out.println("【图片描述】" + image.revisedPrompt());
    }


}
