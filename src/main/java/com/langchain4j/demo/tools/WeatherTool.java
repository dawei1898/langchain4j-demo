package com.langchain4j.demo.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.Random;

/**
 * 天气查询工具
 *
 * @author dawei
 */
public class WeatherTool {

    @Tool("Returns the weather forecast for a given city")
    String getWeather(
            @P("The city for which the weather forecast should be returned") String city
            //TemperatureUnit unit
    ) {
        // 模拟天气查询
        return city + ": 晴，" + new Random().nextInt(30) + "°c";
    }
}
