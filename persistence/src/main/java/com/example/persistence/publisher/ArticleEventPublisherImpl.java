package com.example.persistence.publisher;

import com.example.core.publisher.ArticleEvent;
import com.example.core.publisher.ArticleEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleEventPublisherImpl implements ArticleEventPublisher {
    private final ObjectMapper objectMapper;
    private final StreamBridge streamBridge;

    @SneakyThrows
    @Override
    public void publish(ArticleEvent articleEvent) {
        var message = objectMapper.writeValueAsString(articleEvent);

        streamBridge.send("articles-out-0", message);
    }
}
