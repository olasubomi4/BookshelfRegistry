package com.group5.bookshelfregistry.controller;

import com.group5.bookshelfregistry.annotations.AllowedFileExtension;
import com.group5.bookshelfregistry.annotations.NoAuth;
import com.group5.bookshelfregistry.dto.BookShelfCreationRequest;
import com.group5.bookshelfregistry.service.BookShelfService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/api/book-shelf")
public class BookShelfController {

    @Autowired
    private BookShelfService bookShelfService;

    @NoAuth
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBook (@RequestParam("book") @Valid @NotNull @AllowedFileExtension(value = {"pdf", "doc", "docx"}) MultipartFile book, @RequestParam("isbn") String isbn,
                                         @RequestParam("title") String title , @RequestParam("description") String description,
                                         @RequestParam("categoryId") @Nullable Long categoryId , @RequestParam("author") String author) {
        BookShelfCreationRequest bookShelfCreationRequest= BookShelfCreationRequest.builder().book(book).isbn(isbn)
                .title(title).description(description).categoryId(categoryId).author(author) .build();
        return bookShelfService.createBookShelf(bookShelfCreationRequest);
    }

}
