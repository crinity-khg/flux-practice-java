package com.example.persistence.repository.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@Document(collation = "articles")
@Getter
@ToString
public class ArticleDocument {

    @Id
    private ObjectId id;
    private String title;
    private String content;
    private List<String> thumbnailImageIds;
    private String creatorId;

    public ArticleDocument(String title, String content, List<String> thumbnailImageIds, String creatorId) {
        this(null, title, content, thumbnailImageIds, creatorId);
    }
}