package com.group5.bookshelfregistry.dto.readingProgress;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReadingProgressRequest {
    @NotNull
    private Long bookId;
    @NotNull
    private Long currentPage=0l;
}
