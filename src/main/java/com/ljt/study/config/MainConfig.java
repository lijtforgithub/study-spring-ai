package com.ljt.study.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiJingTang
 * @date 2025-03-20 10:35
 */
@Configuration
public class MainConfig {

    @Bean
    ChatClient defaultSysChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个友好的聊天机器人，用{voice}的声音回答问题。")
//                .defaultOptions()
//                .defaultAdvisors()
                .build();
    }

    @Bean
    MessageChatMemoryAdvisor messageChatMemoryAdvisor() {
        InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        return new MessageChatMemoryAdvisor(chatMemory);
    }

    @Bean
    QuestionAnswerAdvisor questionAnswerAdvisor(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        return new QuestionAnswerAdvisor(vectorStore);
    }

    /**
     * 测试了没效果 异常直接抛给客户端
     */
//    @Bean
    ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
        return new DefaultToolExecutionExceptionProcessor(false);
    }

}
