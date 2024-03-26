package com.group5.bookshelfregistry.dto;

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
    @NotBlank
    private String isbn;
    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String author;
    private Long categoryId;
    private MultipartFile book;
}

