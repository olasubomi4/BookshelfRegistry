package com.group5.bookshelfregistry.dto.bookshelf.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookShelfRequest {
    private Long id;
    @NotBlank(message = "isbn must not be null")
    private String isbn;
    @NotBlank(message = "title must not be null")
    private String title;
    private String description;
    @NotBlank(message = "author must not be null")
    private String author;
    private Long categoryId;
    private MultipartFile book;
    private MultipartFile bookImage;
}

