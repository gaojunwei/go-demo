package com.ahucoding.rocket.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class WeatherService {
    /**
     * SpringAI @Tool注解，方便创建和维护MCP工具
     * name : 定义了工具的名称
     * description : 指明工具的功能
     */
    @Tool(name = "Weather", description = "根据城市名称获取天气预报")
    public String getWeatherByCity(String city) {
        System.out.printf("调用了根据城市名称获取天气预报 > city=%s\n", city);
        Map<String, String> mockData = Map.of(
                "西安", "晴天",
                "北京", "小雨",
                "上海", "大雨",
                "河北", "阴天",
                "邢台", "大暴雨",
                "邯郸", "暴雪"
        );
        return mockData.getOrDefault(city, "抱歉：未查询到对应城市！");
    }

}