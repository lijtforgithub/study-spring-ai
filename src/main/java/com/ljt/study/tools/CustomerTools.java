package com.ljt.study.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Map;

/**
 * @author LiJingTang
 * @date 2025-03-27 14:33
 */
@Slf4j
public class CustomerTools {

    /**
     * 为输入参数定义正确的 required 状态对于降低幻觉风险并确保模型在调用工具时提供正确的输入至关重要。在前面的示例中，email 参数是可选的，这意味着模型可以在不提供值的情况下调用该工具。
     * 如果需要该参数，则模型在调用工具时必须为其提供一个值。如果不存在值，模型可能会编造一个，从而导致幻觉。
     */
    @Tool(description = "Update customer information")
    void updateCustomerInfo(Long id, String name, @ToolParam(required = false) String email) {
        log.info("Updated info for customer with id: " + id);
    }

    /**
     * ToolCallResultConverter
     */
//    @Tool(description = "Retrieve customer information", resultConverter = CustomToolCallResultConverter.class)
//    Customer getCustomerInfo(Long id) {
//        return customerRepository.findById(id);
//    }

    @Tool(description = "Retrieve customer information")
    Customer getCustomerInfo(Long id, ToolContext toolContext) {
        return new CustomerRepository().findById(id, toolContext.getContext().get("tenantId").toString());
    }

    void demo1(ChatModel chatModel) {
        String response = ChatClient.create(chatModel)
                .prompt("Tell me more about the customer with ID 42")
                .tools(new CustomerTools())
                .toolContext(Map.of("tenantId", "acme"))
                .call()
                .content();
    }

    /**
     * 运行时选项优先于默认选项
     */
    void demo2(ChatModel chatModel) {
        ToolCallback[] customerTools = ToolCallbacks.from(new CustomerTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(customerTools)
                .toolContext(Map.of("tenantId", "acme"))
                .build();
        Prompt prompt = new Prompt("Tell me more about the customer with ID 42", chatOptions);
        chatModel.call(prompt);
    }


    static class Customer {
    }

    static class CustomerRepository {
        Customer findById(Long id, String tenantId) {
            return new Customer();
        }
    }

}
