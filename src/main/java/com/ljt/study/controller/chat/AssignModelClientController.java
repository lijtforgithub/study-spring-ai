package com.ljt.study.controller.chat;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LiJingTang
 * @date 2025-03-19 17:41
 */
@Slf4j
@RestController
@RequestMapping("/chat/model")
public class AssignModelClientController {

    private final ChatClient chatClient;

    public AssignModelClientController(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }


    record ActorFilms(String actor, List<String> movies) {}

    @GetMapping("/sync")
    String sync() {
        ActorFilms actorFilms = chatClient.prompt()
                .user("生成一个随机演员的电影记录")
                .call()
                .entity(ActorFilms.class);
        if (Objects.isNull(actorFilms)) {
            return null;
        }
        log.info(actorFilms.actor);
        // 泛型 entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
        return JSON.toJSONString(actorFilms.movies());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> stream(@RequestParam String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .concatWith(Mono.just("[DONE]"));
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ServerSentEvent<String>> sse(@RequestParam String prompt) {
        return chatClient.prompt()
                .messages(new SystemMessage("你是一个搞笑聊天机器人"), new UserMessage(prompt))
                .stream()
                .chatResponse()
                .map(cr -> ServerSentEvent.builder(JSON.toJSONString(cr)).event("message").build());
    }

    @GetMapping(value = "/converter")
    List<ActorFilms> converter() {
        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorFilms>>() {});

        Flux<String> flux = chatClient.prompt()
                .user(u -> u.text("""
                        生成一个随机演员的电影记录
                        {format}
                      """)
                .param("format", converter.getFormat()))
                .stream()
                .content();

        String content = flux.collectList().block().stream().collect(Collectors.joining());
        List<ActorFilms> actorFilms = converter.convert(content);
        return actorFilms;
    }

}
