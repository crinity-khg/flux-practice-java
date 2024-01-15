package com.example.persistence.repository;

import com.example.core.entity.Article;
import com.example.core.entity.ArticleThumbnail;
import com.example.core.repository.ArticleRepository;
import com.example.persistence.common.image.ImageClient;
import com.example.persistence.repository.mongo.document.ArticleDocument;
import com.example.persistence.repository.mongo.repository.ArticleMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMongoRepository articleMongoRepository;
    private final ImageClient imageClient;

    @Override
    public Mono<Article> save(Article article) {
        var documentToSave = fromEntity(article);

        return articleMongoRepository.save(documentToSave)
                .flatMap(articleDocument -> {
                    var imageIds = articleDocument.getThumbnailImageIds();
                    return getThumbnailsByIds(imageIds)
                            .collectList()
                            .map(thumbnails -> fromDocument(articleDocument, thumbnails));
                });
    }

    private ArticleDocument fromEntity(Article article) {
        return new ArticleDocument(
                article.getTitle(),
                article.getContent(),
                article.getThumnails().stream()
                        .map(ArticleThumbnail::getId)
                        .collect(Collectors.toList()),
                article.getCreatorId()
        );
    }

    private Article fromDocument(ArticleDocument articleDocument, List<ArticleThumbnail> thumbnails) {
        return new Article(
                articleDocument.getId().toHexString(),
                articleDocument.getTitle(),
                articleDocument.getContent(),
                thumbnails,
                articleDocument.getCreatorId()
        );
    }

    private Flux<ArticleThumbnail> getThumbnailsByIds(List<String> imageIds) {
        return imageClient.getImagesByIds(imageIds)
                .map(resp -> new ArticleThumbnail(String.valueOf(resp.id()), resp.url(), resp.width(), resp.height()));
    }
}
