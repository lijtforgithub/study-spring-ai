package com.ljt.study.tools;

import com.ljt.study.tools.service.WeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.util.json.schema.JsonSchemaGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author LiJingTang
 * @date 2025-03-26 17:08
 */
public class ToolUseDemo {

    void demo1(ChatModel chatModel) {
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultTools(new DateTimeTools())
                .build();
    }

    void demo2(ChatModel chatModel) {
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt("What day is tomorrow?", chatOptions);
        chatModel.call(prompt);
    }

    void demo3() {
//        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
//        ChatModel chatModel = OllamaChatModel.builder()
//                .ollamaApi(new OllamaApi())
//                .defaultOptions(ToolCallingChatOptions.builder()
//                        .toolCallbacks(dateTimeTools)
//                        .build())
//                .build();
    }

    void demo4() {
        Method method = ReflectionUtils.findMethod(DateTimeTools.class, "getCurrentDateTime");
        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinition.builder(method)
                        .description("Get the current date and time in the user's timezone")
                        .build())
                .toolMethod(method)
                // 如果方法是静态的，则可以省略 toolObject（） 方法，因为它不是必需的。
                .toolObject(new DateTimeTools())
                .build();
    }

    WeatherService weatherService = new WeatherService();
    public static final String CURRENT_WEATHER_TOOL = "currentWeather";
    @Bean(CURRENT_WEATHER_TOOL)
    @Description("Get the weather in location")
    Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> currentWeather() {
        return weatherService;
    }

    void demo5(ChatModel chatModel) {
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolNames("currentWeather")
                .build();
        Prompt prompt = new Prompt("What's the weather like in Copenhagen?", chatOptions);
        chatModel.call(prompt);
    }

    void demo6() {
        ToolDefinition toolDefinition = ToolDefinition.builder()
                .name("currentWeather")
                .description("Get the weather in location")
                .inputSchema("""
        {
            "type": "object",
            "properties": {
                "location": {
                    "type": "string"
                },
                "unit": {
                    "type": "string",
                    "enum": ["C", "F"]
                }
            },
            "required": ["location", "unit"]
        }
    """)
                .build();
    }

    void demo7() {
        Method method = ReflectionUtils.findMethod(DateTimeTools.class, "getCurrentDateTime");
        ToolDefinition toolDefinition = ToolDefinition.builder(method)
                .name("currentDateTime")
                .description("Get the current date and time in the user's timezone")
                .inputSchema(JsonSchemaGenerator.generateForMethodInput(method))
                .build();
    }

    void demo8(ChatModel chatModel) {
        ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new DateTimeTools()))
                .internalToolExecutionEnabled(false)
                .build();
        Prompt prompt = new Prompt("Tell me more about the customer with ID 42", chatOptions);

        ChatResponse chatResponse = chatModel.call(prompt);

        while (chatResponse.hasToolCalls()) {
            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);

            prompt = new Prompt(toolExecutionResult.conversationHistory(), chatOptions);

            chatResponse = chatModel.call(prompt);
        }

        System.out.println(chatResponse.getResult().getOutput().getText());
    }

}
