package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.readingProgress.UpdateReadingProgressRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReadingProgressService {
    public ResponseEntity<?> updateReadingProgress(UpdateReadingProgressRequest updateReadingProgressRequest);
    public ResponseEntity<?> getReadingProgress(Long bookId);

    public ResponseEntity<?>  getAllReadingProgress(String BookName, Pageable pageable);

}
