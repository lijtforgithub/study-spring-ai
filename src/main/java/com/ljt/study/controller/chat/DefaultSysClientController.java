package com.ljt.study.controller.chat;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author LiJingTang
 * @date 2025-03-20 14:14
 */
@RestController
@RequestMapping("/chat/default")
public class DefaultSysClientController {

    @Resource(name = "defaultSysChatClient")
    private ChatClient chatClient;

    @GetMapping("/ai")
    Map<String, String> completion(@RequestParam(value = "prompt", defaultValue = "给我讲个笑话") String prompt,
                                   @RequestParam(value = "voice", defaultValue = "儿童") String voice) {
        return Map.of("completion",
                this.chatClient.prompt()
                        .system(sp -> sp.param("voice", voice))
                        .user(prompt)
                        .call()
                        .content());
    }

}
