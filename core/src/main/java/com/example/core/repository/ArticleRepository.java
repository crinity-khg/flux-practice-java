package com.example.core.repository;

import com.example.core.entity.Article;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

public interface ArticleRepository {

    Mono<Article> save(Article article);
}
