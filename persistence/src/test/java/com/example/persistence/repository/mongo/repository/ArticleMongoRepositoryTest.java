package com.example.persistence.repository.mongo.repository;

import com.example.persistence.repository.mongo.document.ArticleDocument;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Testcontainers
@DataMongoTest
class ArticleMongoRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:5.0.19")
                    .withEnv("MONGO_INITDB_DATABASE", "wooman")
                    .withExposedPorts(27017);

    @DynamicPropertySource
    static void configureProperties(
            DynamicPropertyRegistry registry
    ) {
        var uri = mongoDBContainer.getReplicaSetUrl("wooman");
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    @Autowired
    ArticleMongoRepository articleMongoRepository;

    @Test
    void  saveArticle() throws Exception {
        // given
        var title = "title1";
        var content = "content1";
        var creatorId = "4321";
        var thumbnailImageIds = List.of("1", "2", "3");

        var articleToSave = new ArticleDocument(
                title, content, thumbnailImageIds, creatorId
        );

        // when
        var result = articleMongoRepository.save(articleToSave);

        // then
        StepVerifier.create(result)
                .assertNext(articleDocument -> {
                    assertNotNull(articleDocument.getId());
                    log.info("articleDocument 객체 출력 : {}", articleDocument);
                })
                .verifyComplete();
    }

}