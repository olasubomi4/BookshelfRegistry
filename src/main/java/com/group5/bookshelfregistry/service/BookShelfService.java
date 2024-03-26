package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.BookShelfCreationRequest;
import com.group5.bookshelfregistry.dto.BookShelfCreationResponse;
import org.springframework.http.ResponseEntity;

public interface BookShelfService {

    public ResponseEntity<?> createBookShelf(BookShelfCreationRequest bookShelfCreationRequest);


}
