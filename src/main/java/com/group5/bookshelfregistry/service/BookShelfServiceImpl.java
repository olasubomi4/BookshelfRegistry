package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.BookShelfCreationRequest;
import com.group5.bookshelfregistry.dto.BookShelfCreationResponse;
import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.BookCategory;
import com.group5.bookshelfregistry.repositories.IBookCategoryRepository;
import com.group5.bookshelfregistry.repositories.IBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookShelfServiceImpl implements BookShelfService{
    IBookCategoryRepository iBookCategoryRepository;
    IBookRepository iBookRepository;
    BookUploadService bookUploadService;
    @Override
    public ResponseEntity<?> createBookShelf(BookShelfCreationRequest bookShelfCreationRequest) {
        BookCategory bookCategory= getBookCategoryById(bookShelfCreationRequest.getCategoryId());
        String bookFilesUrl=bookUploadService.uploadBook(bookShelfCreationRequest.getBook());
        Book book= Book.builder().bookCategory(bookCategory).author(bookShelfCreationRequest.getAuthor()).description
                (bookShelfCreationRequest.getDescription()).title(bookShelfCreationRequest.getTitle()).bookLocation(
                        bookFilesUrl).build();
        iBookRepository.save(book);

        BookShelfCreationResponse bookShelfCreationResponse= BookShelfCreationResponse.builder().bookLocation
                        (bookFilesUrl).description(book.getDescription())
                .title(bookShelfCreationRequest.getTitle()).categoryId(getBookCategoryId(bookCategory)).author(book.
                        getAuthor()).isbn(bookShelfCreationRequest.getIsbn()).build();
        return new ResponseEntity<>(bookShelfCreationResponse , HttpStatus.CREATED);
    }

    private BookCategory getBookCategoryById(Long id) {
        if(id==null) {
            return null;
        }
        return iBookCategoryRepository.findById(id).orElse(null);
    }

    private Long getBookCategoryId(BookCategory bookCategory) {
        return Optional.ofNullable(bookCategory).map(id-> id.getId()).orElse(null);
    }

}
