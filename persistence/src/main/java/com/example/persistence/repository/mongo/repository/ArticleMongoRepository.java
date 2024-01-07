package com.example.persistence.repository.mongo.repository;

import com.example.persistence.repository.mongo.document.ArticleDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

public interface ArticleMongoRepository extends ReactiveMongoRepository<ArticleDocument, ObjectId> {

}
