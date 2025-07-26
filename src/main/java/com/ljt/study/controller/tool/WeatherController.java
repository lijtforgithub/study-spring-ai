package com.ljt.study.controller.tool;

import com.ljt.study.tools.DateTimeTools;
import com.ljt.study.tools.WeatherTools;
import com.ljt.study.tools.service.WeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiJingTang
 * @date 2025-03-26 17:25
 */
@RestController
@RequestMapping("/tool/weather")
public class WeatherController {

    private final ChatClient chatClient;

    public WeatherController(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }


    @GetMapping("/func")
    public String func() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("weatherService", new WeatherService())
                .description("查询指定时间指定地点的天气")
                .inputType(WeatherService.WeatherRequest.class)
//                .toolMetadata()
//                .toolCallResultConverter()
                .build();

        return chatClient.prompt()
                .user("合肥明天天气怎么样")
                .tools(toolCallback)
                .call()
                .content();
    }

    @GetMapping("/tools")
    public String tools() {
        return chatClient.prompt()
                .user("明天是几号？合肥明天天气怎么样")
                .tools(new DateTimeTools(), new WeatherTools())
//                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }

}
