package com.ljt.study.controller.tool;

import com.ljt.study.tools.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJingTang
 * @date 2025-03-25 10:14
 */
@RestController
@RequestMapping("/tool/time")
public class DateTimeController {

    private final ChatClient chatClient;

    public DateTimeController(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }


    @GetMapping("/tomorrow")
    public String tomorrow() {
        return chatClient.prompt()
                .user("明天是几号?")
                .tools(new DateTimeTools())
//                .tools(ToolCallbacks.from(new DateTimeTools()))
                .call()
                .content();
    }

    @GetMapping("/alarm")
    public String alarm() {
        return chatClient.prompt()
                .user("设置一个闹钟；现在时间的十分钟之后")
                .tools(new DateTimeTools())
                .call()
                .content();
    }

    @GetMapping("/multiply")
    public String multiply() {
        return chatClient.prompt()
                .user("计算7乘以8的结果")
                .tools(new DateTimeTools())
                .call()
                .content();
    }

}
