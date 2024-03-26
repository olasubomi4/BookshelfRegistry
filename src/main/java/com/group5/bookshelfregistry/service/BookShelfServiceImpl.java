package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.BookShelfRequest;
import com.group5.bookshelfregistry.dto.BookShelfResponse;
import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.BookCategory;
import com.group5.bookshelfregistry.repositories.IBookCategoryRepository;
import com.group5.bookshelfregistry.repositories.IBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import static com.group5.bookshelfregistry.enums.ResponseDefinition.FAILED_UNABLE_TO_DELETE_BOOK;
import static com.group5.bookshelfregistry.enums.ResponseDefinition.SUCCESSFUL;

@Service
@AllArgsConstructor
public class BookShelfServiceImpl implements BookShelfService{
    IBookCategoryRepository iBookCategoryRepository;
    IBookRepository iBookRepository;
    BookUploadService bookUploadService;
    @Override
    public ResponseEntity<?> createBookShelf(BookShelfRequest bookShelfRequest) {
        BookCategory bookCategory= getBookCategoryById(bookShelfRequest.getCategoryId());
        String bookFilesUrl=bookUploadService.uploadBook(bookShelfRequest.getBook());
        Book book= Book.builder().bookCategory(bookCategory).author(bookShelfRequest.getAuthor()).description
                (bookShelfRequest.getDescription()).title(bookShelfRequest.getTitle()).bookLocation(
                        bookFilesUrl).isbn(bookShelfRequest.getIsbn()).build();
        iBookRepository.save(book);

        BookShelfResponse bookShelfResponse = BookShelfResponse.builder().bookLocation
                        (bookFilesUrl).description(book.getDescription())
                .title(bookShelfRequest.getTitle()).categoryId(getBookCategoryId(bookCategory)).author(book.
                        getAuthor()).isbn(bookShelfRequest.getIsbn()).message(SUCCESSFUL.getMessage()).success(SUCCESSFUL.getSuccessful()).build();
        return new ResponseEntity<>(bookShelfResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateBookShelf(BookShelfRequest bookShelfRequest) {
        Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        if (bookShelfRequest.getBook() != null) {
            String bookFilesUrl = bookUploadService.updateBook(bookShelfRequest.getBook(),existingBook.getBookLocation());
            existingBook.setBookLocation(bookFilesUrl);
        }

        if (bookShelfRequest.getIsbn() != null) {
            existingBook.setIsbn(bookShelfRequest.getIsbn());
        }

        if (bookShelfRequest.getTitle() != null) {
            existingBook.setTitle(bookShelfRequest.getTitle() );
        }

        if (bookShelfRequest.getDescription() != null) {
            existingBook.setDescription(bookShelfRequest.getDescription());
        }

        if (bookShelfRequest.getCategoryId() != null) {
            BookCategory bookCategory = getBookCategoryById(bookShelfRequest.getCategoryId());
            existingBook.setBookCategory(bookCategory);
        }

        if (bookShelfRequest.getAuthor() != null) {
            existingBook.setAuthor(bookShelfRequest.getAuthor());
        }

        BookShelfResponse bookShelfResponse = BookShelfResponse.builder().bookLocation(existingBook.getBookLocation())
                .description(existingBook.getDescription()).title(existingBook.getTitle()).categoryId(
                        getBookCategoryId(existingBook.getBookCategory())).author(existingBook.
                        getAuthor()).isbn(existingBook.getIsbn()).build();
        return new ResponseEntity<>(bookShelfResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getBookShelf(BookShelfRequest bookShelfRequest) {
        Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        BookShelfResponse bookShelfResponse = BookShelfResponse.builder().bookLocation(existingBook.getBookLocation())
                .description(existingBook.getDescription()).title(existingBook.getTitle()).categoryId(
                        getBookCategoryId(existingBook.getBookCategory())).author(existingBook.
                        getAuthor()).isbn(existingBook.getIsbn()).message(SUCCESSFUL.getMessage()).success(SUCCESSFUL.getSuccessful()).build();
        return new ResponseEntity<>(bookShelfResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getBookShelfs(BookShelfRequest bookShelfRequest, Pageable pageable) {

        Page<Book> books = iBookRepository.findByCategoryIdAndAuthorAndTitleAndIsbn(
                bookShelfRequest.getCategoryId(),
                bookShelfRequest.getAuthor(),
                bookShelfRequest.getTitle(),
                bookShelfRequest.getIsbn(),
                pageable);
        if(books.isEmpty()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(books);

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

    @Override
    public ResponseEntity<?> deleteBookShelf(BookShelfRequest bookShelfRequest) {
        Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (existingBook.getBookLocation() != null) {
            if(!bookUploadService.deleteBook(existingBook.getBookLocation()));{
                BookShelfResponse bookShelfResponse = BookShelfResponse.builder().bookLocation(existingBook.getBookLocation())
                        .description(existingBook.getDescription()).title(existingBook.getTitle()).categoryId(
                                getBookCategoryId(existingBook.getBookCategory())).author(existingBook.
                                getAuthor()).isbn(existingBook.getIsbn()).message(FAILED_UNABLE_TO_DELETE_BOOK.getMessage())
                        .success(FAILED_UNABLE_TO_DELETE_BOOK.getSuccessful()).build();
                return new ResponseEntity<>(bookShelfResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        iBookRepository.delete(existingBook);
        BookShelfResponse bookShelfResponse = BookShelfResponse.builder().bookLocation(existingBook.getBookLocation())
                .description(existingBook.getDescription()).title(existingBook.getTitle()).categoryId(
                        getBookCategoryId(existingBook.getBookCategory())).author(existingBook.
                        getAuthor()).isbn(existingBook.getIsbn()).message(SUCCESSFUL.getMessage()).success(SUCCESSFUL.getSuccessful()).build();
        return new ResponseEntity<>(bookShelfResponse, HttpStatus.OK);

    }

}
