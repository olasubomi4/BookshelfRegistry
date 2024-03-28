package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.bookshelf.request.BookShelfRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BookShelfService {
    public ResponseEntity<?> createBookShelf(BookShelfRequest bookShelfRequest);
    public ResponseEntity<?> updateBookShelf(BookShelfRequest bookShelfRequest);
    public ResponseEntity<?> deleteBookShelf(BookShelfRequest bookShelfRequest);
    public ResponseEntity<?> getBookShelf(BookShelfRequest bookShelfRequest);
    public ResponseEntity<?> getBookShelfs(BookShelfRequest bookShelfRequest, Pageable pageable);



}
