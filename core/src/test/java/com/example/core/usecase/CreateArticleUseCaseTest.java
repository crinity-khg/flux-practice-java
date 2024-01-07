package com.example.core.usecase;

import com.example.core.entity.Article;
import com.example.core.entity.ArticleThumbnail;
import com.example.core.entity.ArticleThumbnailIdOnly;
import com.example.core.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateArticleUseCaseTest {

    @InjectMocks
    CreateArticleUseCase createArticleUseCase;

    @Mock
    ArticleRepository articleRepository;

    @Test
    void happyCase() {
        // given
        var newArticleId = "1";
        var title = "title1";
        var content = "content1";
        var creatorId = "4321";
        var thumbnailImageId = List.of("1", "2", "3");

        var savedArticle = new Article(
                newArticleId, title, content,
                thumbnailImageId.stream()
                        .map(ArticleThumbnailIdOnly::new)
                        .collect(Collectors.toList()),
                creatorId
        );
        given(articleRepository.save(any()))
                .willReturn(Mono.just(savedArticle));

        // when
        var input = new CreateArticleUseCase.Input(
                title, content, thumbnailImageId, creatorId
        );
        var result = createArticleUseCase.execute(input);

        // then
        StepVerifier.create(result)
                .assertNext(article -> {
                    assertEquals(newArticleId, article.getId());
                    assertEquals(title, article.getTitle());
                    assertEquals(content, article.getContent());
                    assertEquals(creatorId, article.getCreatorId());

                    var actualImageIds = article.getThumnails()
                            .stream()
                            .map(ArticleThumbnail::getId)
                            .collect(Collectors.toList());

                    assertEquals(thumbnailImageId, actualImageIds);
                })
                .verifyComplete();
    }

}