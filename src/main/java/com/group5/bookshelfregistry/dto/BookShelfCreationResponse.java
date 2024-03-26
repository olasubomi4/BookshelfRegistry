package com.group5.bookshelfregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookShelfCreationResponse  extends Response{
    private String isbn;
    private String title;
    private String description;
    private String author;
    private Long categoryId;
    private String bookLocation;
}

