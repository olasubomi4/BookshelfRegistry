package com.group5.bookshelfregistry.dto.bookCategory.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryResponseData {
    private Long id;
    private String categoryName;
}
