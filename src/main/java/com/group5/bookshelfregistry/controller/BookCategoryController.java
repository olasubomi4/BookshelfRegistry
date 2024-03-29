package com.group5.bookshelfregistry.controller;

import com.group5.bookshelfregistry.annotations.NoAuth;
import com.group5.bookshelfregistry.dto.bookCategory.request.BookCategoryRequest;
import com.group5.bookshelfregistry.service.BookCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/book-category")
public class BookCategoryController {

    @Autowired
    BookCategoryService bookCategoryService;
    @NoAuth
    @PostMapping
    public ResponseEntity<?> createBookCategory(@RequestBody BookCategoryRequest bookCategoryRequest) {
        return bookCategoryService.createBookCategory(bookCategoryRequest);
    }

    @NoAuth
    @GetMapping({"/{id}"})
    public ResponseEntity<?> getBookCategory(@PathVariable("id") Long id) {
        return bookCategoryService.getBookCategory(id);
    }
    @NoAuth
    @GetMapping
    public ResponseEntity<?> getBookCategories( @RequestParam(value = "categoryName",required = false) String categoryName,  @RequestParam(value = "limit",defaultValue = "10") int limit,@RequestParam
            (value = "offset",defaultValue = "0") int offset) {
        BookCategoryRequest bookCategoryRequest= BookCategoryRequest.builder().categoryName(categoryName)
                .build();
        Pageable page= PageRequest.of(offset,limit);
        return bookCategoryService.getBookCategories(bookCategoryRequest,page);
    }

    @NoAuth
    @PatchMapping({"/{id}"})
    public ResponseEntity<?> updateBookCategory(@PathVariable("id") Long id, @RequestBody @Valid BookCategoryRequest bookCategoryRequest) {
        bookCategoryRequest.setId(id);
        return bookCategoryService.updateBookCategory(bookCategoryRequest);
    }
}
