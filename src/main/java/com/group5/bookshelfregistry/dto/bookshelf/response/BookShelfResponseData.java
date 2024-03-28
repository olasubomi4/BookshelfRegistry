package com.group5.bookshelfregistry.dto.bookshelf.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookShelfResponseData {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private String author;
    private Long categoryId;
    private String bookLocation;
}

