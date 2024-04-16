package com.group5.bookshelfregistry.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long numberOfBooksReservedByUser;
    private Long numberOfBooksCreatedByUser;
    private Long numberOfBookDeletedByUser;
}
