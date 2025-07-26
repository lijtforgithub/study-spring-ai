//package com.ljt.study.controller.mcp;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
//import org.springframework.ai.tool.ToolCallback;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author LiJingTang
// * @date 2025-03-31 15:39
// */
//@Slf4j
//@RestController
//@RequestMapping("/mcp")
//public class McpController {
//
//    private final ChatClient chatClient;
//
//    public McpController(ChatClient.Builder chatClientBuilder, AsyncMcpToolCallbackProvider tools) {
//        for (ToolCallback toolCallback : tools.getToolCallbacks()) {
//            log.info("MCP-Server: {}", toolCallback.getDescription());
//        }
//
//        this.chatClient = chatClientBuilder
//                .defaultTools(tools)
//                .build();
//    }
//
//    @GetMapping("/weather")
//    String weather() {
//        return chatClient.prompt()
//                .user("合肥天气怎么样")
//                .call()
//                .content();
//    }
//
//}
