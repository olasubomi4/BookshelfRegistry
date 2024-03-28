package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.bookCategory.request.BookCategoryRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BookCategoryService {

    public ResponseEntity<?> createBookCategory(BookCategoryRequest bookCategoryRequest);
    public ResponseEntity<?> getBookCategory(Long id);
    public ResponseEntity<?> getBookCategories(BookCategoryRequest bookCategoryRequest, Pageable pageable);
    public ResponseEntity<?> deleteBookCategory(Long id);

    public ResponseEntity<?> updateBookCategory(BookCategoryRequest bookCategoryRequest);



}
