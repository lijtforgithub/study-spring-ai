package com.ljt.study.controller.chat;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * MessageChatMemoryAdvisor
 * QuestionAnswerAdvisor
 *
 * @author LiJingTang
 * @date 2025-03-20 10:27
 */
@RestController
@RequestMapping("/chat/advisor")
public class AdvisorClientController {

    private final ChatClient chatClient;

    @Resource
    private MessageChatMemoryAdvisor memoryAdvisor;

    public AdvisorClientController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/simpleLog")
    String simpleLog(@RequestParam String prompt) {
        return chatClient.prompt()
                // log添加到链的末尾
                .advisors(new SimpleLoggerAdvisor())
                .user(prompt)
                .call()
                .content();
    }

    @GetMapping("/customLog")
    ChatResponse customLog(@RequestParam String prompt) {
        SimpleLoggerAdvisor customLogger = new SimpleLoggerAdvisor(
                request -> "Custom request: " + request.userText(),
                response -> "Custom response: " + Optional.ofNullable(response)
                        .map(ChatResponse::getResult)
                        .map(Generation::getOutput)
                        .map(AbstractMessage::getText)
                        .orElse(null),
                0
        );

        return chatClient.prompt()
                .advisors(customLogger)
                .user(prompt)
                .call()
                .chatResponse();
    }

    @GetMapping("/memory")
    String memory(@RequestParam String chatId, @RequestParam String prompt) {
        return chatClient.prompt()
                .advisors(memoryAdvisor)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5))
                .user(prompt)
                .call()
                .content();
    }

}
