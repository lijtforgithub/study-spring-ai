package com.ljt.study.controller.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJingTang
 * @date 2025-03-20 10:27
 */
@RestController
@RequestMapping("/chat/auto")
public class AutoConfigClientController {

    private final ChatClient chatClient;

    public AutoConfigClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/hello")
    String hello(@RequestParam String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @GetMapping("/resp")
    ChatResponse resp(@RequestParam String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .chatResponse();
    }

}
