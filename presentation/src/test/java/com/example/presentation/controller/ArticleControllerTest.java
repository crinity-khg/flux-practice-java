package com.example.presentation.controller;

import com.example.core.entity.Article;
import com.example.core.entity.ArticleThumbnail;
import com.example.core.entity.ArticleThumbnailIdOnly;
import com.example.core.usecase.CreateArticleUseCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;

@Slf4j
@AutoConfigureWebTestClient
@WebFluxTest(controllers = {ArticleController.class})
class ArticleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CreateArticleUseCase createArticleUseCase;

    @Test
    void createArticle() {
        // given
        var newArticleId = "abcd";
        var title = "title1";
        var content = "content1";
        var creatorUserId = "4321";
        var thumbnailImageIds = List.of("1", "2", "3");
        var thumbnails = thumbnailImageIds.stream()
                .map(it -> (ArticleThumbnail) new ArticleThumbnailIdOnly(it))
                .collect(Collectors.toList());

        var articleToReturn = new Article(
                newArticleId, title, content, thumbnails, creatorUserId
        );
        given(createArticleUseCase.execute(any()))
                .willReturn(Mono.just(articleToReturn));

        // when
        var body = "{\"title\": \"" + title + "\"," +
                "\"content\": \"" + content + "\"," +
                "\"thumbnailImageIds\": [\"1\", \"2\", \"3\"]}";

        var result = webTestClient.post()
                .uri("/api/v1/articles")
                .bodyValue(body)
                .header("Content-Type", "application/json")
                .header("X-User-Id", creatorUserId)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo(newArticleId)
                .jsonPath("$.title").isEqualTo(title)
                .jsonPath("$.content").isEqualTo(content)
                .jsonPath("$.creatorId").isEqualTo(creatorUserId)
                .returnResult();

        // then
        log.info(new String(result.getResponseBody()));
    }
}