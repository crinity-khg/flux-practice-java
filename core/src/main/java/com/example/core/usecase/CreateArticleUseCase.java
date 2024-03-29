package com.example.core.usecase;

import com.example.core.entity.Article;
import com.example.core.entity.ArticleThumbnail;
import com.example.core.entity.ArticleThumbnailIdOnly;
import com.example.core.publisher.ArticleCreatedEvent;
import com.example.core.publisher.ArticleEventPublisher;
import com.example.core.repository.ArticleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateArticleUseCase {

    private final ArticleRepository articleRepository;
    private final ArticleEventPublisher articleEventPublisher;


    @Data
    public static class Input {
        private final String title;
        private final String content;
        private final List<String> thumbnailImageIds;
        private final String creatorId;
    }

    public Mono<Article> execute(Input input) {
        List<ArticleThumbnail> thumbnails = input.thumbnailImageIds
                .stream()
                .map(ArticleThumbnailIdOnly::new)
                .collect(Collectors.toList());

        var newArticle = Article.create(
                input.title,
                input.content,
                thumbnails,
                input.creatorId
        );

        return articleRepository.save(newArticle)
                .doOnNext(article -> {
                    var event = new ArticleCreatedEvent(article.getId(), article.getCreatorId());
                    articleEventPublisher.publish(event);
                });
    }
}
