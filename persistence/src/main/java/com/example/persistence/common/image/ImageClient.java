package com.example.persistence.common.image;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageClient {

    private final WebClient imageWebclient;
    private final ReactiveCircuitBreakerFactory cb;

    private final static ImageResponse defaultImage =
            new ImageResponse(0L, "http://grizz.com/images/default", 100, 100);

    public Flux<ImageResponse> getImagesByIds(List<String> imageIds) {
        String param = String.join(",", imageIds);

        var flux = imageWebclient.get()
                .uri("/api/images?imageIds=" + param)
                .retrieve()
                .bodyToFlux(ImageResponse.class);

        return flux.transform(it -> cb.create("image").run(it, e ->
                Flux.create(emitter -> {
                    imageIds.forEach(ids -> emitter.next(defaultImage));
                    emitter.complete();
                })));
    }
}
