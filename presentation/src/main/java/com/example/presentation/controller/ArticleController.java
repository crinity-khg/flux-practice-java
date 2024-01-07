package com.example.presentation.controller;

import com.example.core.entity.Article;
import com.example.core.entity.ArticleThumbnail;
import com.example.core.usecase.CreateArticleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleController {

    private final CreateArticleUseCase createArticleUseCase;

    @PostMapping("/articles")
    public Mono<ArticleResponse> createArticle(
            @RequestBody CreateArticleRequest createArticleRequest,
            @RequestHeader("X-User-Id") String userId
    ) {

        var input = new CreateArticleUseCase.Input(
                createArticleRequest.title(),
                createArticleRequest.content(),
                createArticleRequest.thumbnailImageIds(),
                userId
        );

        return createArticleUseCase.execute(input)
                .map(this::fromEntity);
    }

    private ArticleResponse fromEntity(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatorId(),
                article.getThumnails()
                        .stream()
                        .map(this::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    private ThumbnailResponse fromEntity(ArticleThumbnail thumbnail) {
        return new ThumbnailResponse(
                thumbnail.getId(),
                thumbnail.getUrl(),
                thumbnail.getWidth(),
                thumbnail.getHeight()
        );
    }
}
