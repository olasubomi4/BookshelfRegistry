package com.group5.bookshelfregistry.dto.bookCategory.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryRequest {
    private Long id;
    @NotBlank(message = "category name cant be blank")
    private String categoryName;
}
