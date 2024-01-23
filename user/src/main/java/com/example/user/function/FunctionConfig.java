package com.example.user.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

@Configuration
public class FunctionConfig {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    Function<Flux<String>, Flux<String>> followerNoti(ObjectMapper objectMapper) {
        return input -> {
            return input.flatMap(rawEvent -> {
                try {
                    var event = objectMapper.readValue(rawEvent, ArticleCreatedEvent.class);
                    var creatorId = event.getCreatorUserId();
                    var followers = getFollowerIds(creatorId);

                    var notiMessages = getSendNotiMessages(creatorId, followers, objectMapper);
                    return Flux.fromIterable(notiMessages);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }

    private List<String> getFollowerIds(String creatorId) {
        return List.of(
                creatorId + "1",
                creatorId + "2",
                creatorId + "3"
        );
    }

    private List<String> getSendNotiMessages(String creatorId, List<String> followers, ObjectMapper objectMapper) {
        var notiMessage = creatorId + "님이 소식을 작성했어요!";

        return followers.stream()
                .map(followerId -> {
                    var message = new SendNotiMessage(followerId, notiMessage);
                    try {
                        return objectMapper.writeValueAsString(message);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
