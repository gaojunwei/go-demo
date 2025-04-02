package com.ahucoding.rocket.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherService {

    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherByCity(String city) {
        System.out.printf("调用了根据城市名称获取天气预报 > city=%s\n", city);
        Map<String, String> mockData = Map.of(
                "西安", "晴天",
                "北京", "小雨",
                "上海", "大雨"
        );
        return mockData.getOrDefault(city, "抱歉：未查询到对应城市！");
    }

}