package com.ahucoding.rocket.callmcpserver.view;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import io.modelcontextprotocol.client.McpSyncClient;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author jianzhang
 * 2025/03/18/下午8:00
 */
@RestController
@RequestMapping("/dashscope/chat-client")
@CrossOrigin("*")
public class ChatController {

    private final ChatClient chatClient;

    private final ChatMemory chatMemory = new InMemoryChatMemory();


    public ChatController(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients, ToolCallbackProvider tools) {
        this.chatClient = chatClientBuilder
                // 它定义了聊天机器人在回答问题时应当遵循的风格和角色定位。
                .defaultSystem("以专业天气预报主持人的风格回答问题。")
                // 指定聊天客户端可用的工具
                .defaultTools(tools)
                .defaultOptions(DashScopeChatOptions.builder().withTopP(0.7).build())
                .build();
    }



    @RequestMapping(value = "/generate_stream", method = RequestMethod.GET)
    public Flux<ChatResponse> generateStream(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("prompt") String prompt) {
        response.setCharacterEncoding("UTF-8");
        var messageChatMemoryAdvisor = new MessageChatMemoryAdvisor(chatMemory, id, 10);
        return this.chatClient.prompt()
                .user(prompt)
                .advisors(messageChatMemoryAdvisor)
                .stream()
                .chatResponse()
                .onErrorResume(e -> {
                    System.out.println("Error: " + e.getMessage());
                    return Mono.empty();
                });
    }


    @GetMapping("/advisor/chat/{id}/{prompt}")
    public Flux<String> advisorChat(
            HttpServletResponse response,
            @PathVariable String id,
            @PathVariable String prompt) {

        response.setCharacterEncoding("UTF-8");
        var messageChatMemoryAdvisor = new MessageChatMemoryAdvisor(chatMemory, id, 10);
        return this.chatClient.prompt()
                .user(prompt)
                .advisors(messageChatMemoryAdvisor).stream().content();
    }



}
