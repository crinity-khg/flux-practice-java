package com.example.persistence.repository;

import com.example.core.entity.Article;
import com.example.core.entity.ArticleThumbnail;
import com.example.core.entity.ArticleThumbnailIdOnly;
import com.example.core.repository.ArticleRepository;
import com.example.persistence.repository.mongo.document.ArticleDocument;
import com.example.persistence.repository.mongo.repository.ArticleMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMongoRepository articleMongoRepository;

    @Override
    public Mono<Article> save(Article article) {
        var documentToSave = fromEntity(article);

        return articleMongoRepository.save(documentToSave)
                .map(this::fromDocument);
    }

    private ArticleDocument fromEntity(Article article) {
        return new ArticleDocument(
                article.getId(),
                article.getContent(),
                article.getThumnails().stream()
                        .map(ArticleThumbnail::getId)
                        .collect(Collectors.toList()),
                article.getCreatorId()
        );
    }

    private Article fromDocument(ArticleDocument articleDocument) {
        return new Article(
                articleDocument.getId().toHexString(),
                articleDocument.getTitle(),
                articleDocument.getContent(),
                articleDocument.getThumbnailImageIds().stream()
                        .map(ArticleThumbnailIdOnly::new)
                        .collect(Collectors.toList()),
                articleDocument.getCreatorId()
        );
    }
}
