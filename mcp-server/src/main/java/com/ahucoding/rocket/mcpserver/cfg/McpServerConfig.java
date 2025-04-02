package com.ahucoding.rocket.mcpserver.cfg;

import com.ahucoding.rocket.mcpserver.service.BookService;
import com.ahucoding.rocket.mcpserver.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Map;

/**
 * @author jianzhang
 * 2025/03/18/下午3:23
 */
@Configuration
@EnableWebMvc
public class McpServerConfig implements WebMvcConfigurer {

    // 允许服务器公开可由语言模型调用的工具
    @Bean
    public ToolCallbackProvider openLibraryToolsOne(BookService bookService) {
        return MethodToolCallbackProvider.builder().toolObjects(bookService).build();
    }

    // 允许服务器公开可由语言模型调用的工具
    @Bean
    public ToolCallbackProvider openLibraryToolsTwo(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }

    // 允许服务器公开可由语言模型调用的工具
    @Bean
    public ToolCallbackProvider openLibraryToolsThree() {
        return ToolCallbackProvider.from(Arrays.asList(toolCallbackOne, toolCallbackTwo));
    }

    private ToolCallback toolCallbackOne = new ToolCallback() {
        @Override
        public ToolDefinition getToolDefinition() {
            return ToolDefinition.builder()
                    .name("Weather1")
                    .description("大龙电台，根据城市名称获取天气预报")
                    .inputSchema("""
                        {
                            "type": "object",
                            "properties": {
                                "cityName": {
                                    "type": "string",
                                    "description": "城市名称，例如：西安、北京、上海等"
                                }
                            },
                            "required": ["cityName"]
                        }
                        """)
                    .build();
        }
        public record TianQi(String cityName) {
        }

        @Override
        public String call(String toolInput) {
            ObjectMapper objectMapper = new ObjectMapper();
            TianQi tq= null;
            try {
                tq = objectMapper.readValue(toolInput,TianQi.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> mockData = Map.of(
                    "西安", "晴天",
                    "北京", "小雨",
                    "上海", "大雨",
                    "河北", "阴天",
                    "邢台", "大暴雨",
                    "邯郸", "暴雪"
            );
            System.out.println("ToolCallback 大龙电台,天气服务，查询城市："+tq.cityName);
            return mockData.getOrDefault(tq.cityName, "抱歉：未查询到对应城市！");
        }
    };

    private ToolCallback toolCallbackTwo = new ToolCallback() {
        @Override
        public ToolDefinition getToolDefinition() {
            return ToolDefinition.builder()
                    .name("Weather2")
                    .description("巨龙电台，根据城市名称获取天气预报")
                    .inputSchema("""
                        {
                            "type": "object",
                            "properties": {
                                "cityName": {
                                    "type": "string",
                                    "description": "城市名称，例如：西安、北京、上海等"
                                }
                            },
                            "required": ["cityName"]
                        }
                        """)
                    .build();
        }
        public record TianQi(String cityName) {
        }

        @Override
        public String call(String toolInput) {
            ObjectMapper objectMapper = new ObjectMapper();
            TianQi tq= null;
            try {
                tq = objectMapper.readValue(toolInput,TianQi.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> mockData = Map.of(
                    "西安", "晴天",
                    "北京", "小雨",
                    "上海", "大雨",
                    "河北", "阴天",
                    "邢台", "大暴雨",
                    "邯郸", "暴雪"
            );
            System.out.println("ToolCallback 巨龙电台,天气服务，查询城市："+tq.cityName);
            return mockData.getOrDefault(tq.cityName, "抱歉：未查询到对应城市！");
        }
    };
}
