package com.langchain4j.demo.test.image;

import com.alibaba.fastjson2.JSON;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static dev.langchain4j.model.openai.OpenAiImageModelName.DALL_E_2;
import static dev.langchain4j.model.openai.OpenAiImageModelName.DALL_E_3;

/**
 * 生成图像测试
 *
 * @author dawei
 */

@SpringBootTest
@ActiveProfiles("dev")
public class ImageModelTest {

    @Resource
    private ImageModel imageModel;

    @Test
    public void testImageModel() {
        String message = "一艘航空母舰在大海快速前进";
        System.out.println("【提问】 = " + message);

        Response<Image> response = imageModel.generate(message);

        Image image = response.content();
        System.out.println("image = " + JSON.toJSONString(image));
        // https://oaidalleapiprodscus.blob.core.windows.net/private/org-9EBadt1fHMP7kZvWmuXFzQ7j/user-cV9LfNkUwLKV9wwkWaooDg3C/img-K2bbnWXw6OYAMvbtQUud3Ds8.png?st=2025-09-28T01%3A44%3A42Z&se=2025-09-28T03%3A44%3A42Z&sp=r&sv=2024-08-04&sr=b&rscd=inline&rsct=image/png&skoid=6e4237ed-4a31-4e1d-a677-4df21834ece0&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2025-09-28T02%3A44%3A42Z&ske=2025-09-29T02%3A44%3A42Z&sks=b&skv=2024-08-04&sig=1pp%2B/Hrn57l9BBwsJwLR8AjuX4Ufy0Q5sNWTd1XMjL8%3D
        System.out.println("【生成图像】 = " + image.url());
    }

}
