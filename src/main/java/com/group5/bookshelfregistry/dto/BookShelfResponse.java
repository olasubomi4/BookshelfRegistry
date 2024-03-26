package com.group5.bookshelfregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BookShelfResponse extends Response{
    private String isbn;
    private String title;
    private String description;
    private String author;
    private Long categoryId;
    private String bookLocation;


    @Builder
    public BookShelfResponse(Boolean success, String message, String isbn, String title, String description, String author, Long categoryId, String bookLocation) {
        super(success, message);
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.author = author;
        this.categoryId = categoryId;
        this.bookLocation = bookLocation;
    }
}

