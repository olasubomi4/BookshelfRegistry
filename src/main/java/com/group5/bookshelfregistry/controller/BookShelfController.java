package com.group5.bookshelfregistry.controller;

import com.group5.bookshelfregistry.annotations.AllowedFileExtension;
import com.group5.bookshelfregistry.annotations.NoAuth;
import com.group5.bookshelfregistry.dto.bookshelf.request.BookShelfRequest;
import com.group5.bookshelfregistry.dto.reserveBook.ReserveBookRequest;
import com.group5.bookshelfregistry.service.BookShelfService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/book-shelf")
public class BookShelfController {

    @Autowired
    private BookShelfService bookShelfService;

    @Operation(summary = "Reserve a Book",
            description = "This endpoint is used to reserve a book for a user. This endpoint requires a book id " +
                    "to be passed")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN')")
    @PostMapping(value = "reserve-book",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reserveBook (@RequestBody ReserveBookRequest reserveBookRequest) {
        return bookShelfService.reserveBook(reserveBookRequest);
    }

    @Operation(summary = "Get Books reserved by this user",
            description = "This endpoint is used to get all the books reserved by the currently logged-in user.")
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN')")
    @GetMapping(value = "reserve-book")
    public ResponseEntity<?> getReserveBooks (@RequestParam(value = "limit",defaultValue = "10") int limit,@RequestParam
            (value = "offset",defaultValue = "0") int offset) {
        Pageable page= PageRequest.of(offset,limit);
        return bookShelfService.getReservedBooks(page);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBook (@RequestParam("book")  @AllowedFileExtension(value = {"pdf", "doc", "docx"}) MultipartFile book,@RequestParam(value = "bookImage") @AllowedFileExtension(value = {"jpeg", "png","jpg"}) MultipartFile bookImage, @RequestParam("isbn") String isbn,
                                         @RequestParam("title") String title , @RequestParam("description") String description,
                                         @RequestParam("categoryId") @Nullable Long categoryId , @RequestParam("author") String author) {
        BookShelfRequest bookShelfRequest = BookShelfRequest.builder().book(book).isbn(isbn)
                .title(title).description(description).categoryId(categoryId).author(author).bookImage(bookImage).build();
        return bookShelfService.createBookShelf(bookShelfRequest);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBook (@RequestParam(value = "book",required = false) @AllowedFileExtension(value = {"pdf", "doc", "docx"}) MultipartFile book,@RequestParam(value = "bookImage",required = false) @AllowedFileExtension(value = {"jpeg", "png","jpg"}) MultipartFile bookImage, @RequestParam(value="isbn" ,required = false) String isbn,
                                         @RequestParam(value="title",required = false) String title , @RequestParam(value="description",required = false) String description,
                                         @RequestParam(value = "categoryId",required = false) Long categoryId , @RequestParam(value="author",required = false) String author,@RequestParam(value="id") Long id) {
        BookShelfRequest bookShelfRequest = BookShelfRequest.builder().id(id).book(book).isbn(isbn)
                .title(title).description(description).categoryId(categoryId).author(author).bookImage(bookImage).build();
        return bookShelfService.updateBookShelf(bookShelfRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> deleteBook (@PathVariable(value="id") Long id) {
        BookShelfRequest bookShelfRequest = BookShelfRequest.builder().id(id).build();
        return bookShelfService.deleteBookShelf(bookShelfRequest);
    }

//    @NoAuth
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN')")
    @GetMapping({"/{id}"})
    public ResponseEntity<?> getBook (@PathVariable("id") Long id) {
        BookShelfRequest bookShelfRequest = BookShelfRequest.builder().id(id).build();
        return bookShelfService.getBookShelf(bookShelfRequest);
    }

//    @NoAuth
    @PreAuthorize("hasAnyRole('VIEWER','ADMIN')")
    @GetMapping
    public ResponseEntity<?> getBooks (@RequestParam(value="categoryId", required = false) Long categoryId,@RequestParam(value="author",required = false) String author,
                                       @RequestParam(value="title",required = false) String title,@RequestParam(value="isbn",required = false) String isbn,
                                       @RequestParam(value = "limit",defaultValue = "10") int limit,@RequestParam
                                                   (value = "offset",defaultValue = "0") int offset ) {
        BookShelfRequest bookShelfRequest = BookShelfRequest.builder().categoryId(categoryId).author(author).title(title)
                .isbn(isbn).build();
        Pageable page= PageRequest.of(offset,limit);
        return bookShelfService.getBookShelfs(bookShelfRequest,page);
    }



}
