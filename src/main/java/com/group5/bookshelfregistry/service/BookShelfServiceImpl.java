package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.bookshelf.request.BookShelfRequest;
import com.group5.bookshelfregistry.dto.BaseResponse;
import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.BookCategory;
import com.group5.bookshelfregistry.repositories.IBookCategoryRepository;
import com.group5.bookshelfregistry.repositories.IBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            BookCategory bookCategory = getBookCategoryById(bookShelfRequest.getCategoryId());
            String bookFilesUrl = bookUploadService.uploadBook(bookShelfRequest.getBook());
            Book book = Book.builder().bookCategory(bookCategory).author(bookShelfRequest.getAuthor()).description
                    (bookShelfRequest.getDescription()).title(bookShelfRequest.getTitle()).bookLocation(
                    bookFilesUrl).isbn(bookShelfRequest.getIsbn()).build();
            iBookRepository.save(book);

            BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                    SUCCESSFUL.getSuccessful()).data(book).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException dataIntegrityViolationException) {
            BaseResponse baseResponse = BaseResponse.builder()
                           .message("Book with the same title or ISBN already exists.").success(false).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> updateBookShelf(BookShelfRequest bookShelfRequest) {
       try {
           Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


           if (bookShelfRequest.getBook() != null) {
               String bookFilesUrl = bookUploadService.updateBook(bookShelfRequest.getBook(), existingBook.getBookLocation());
               existingBook.setBookLocation(bookFilesUrl);
           }

           if (bookShelfRequest.getIsbn() != null) {
               existingBook.setIsbn(bookShelfRequest.getIsbn());
           }

           if (bookShelfRequest.getTitle() != null) {
               existingBook.setTitle(bookShelfRequest.getTitle());
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
           iBookRepository.save(existingBook);

           BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                   SUCCESSFUL.getSuccessful()).data(existingBook).build();
           return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
       }
       catch (DataIntegrityViolationException dataIntegrityViolationException) {
           BaseResponse baseResponse = BaseResponse.builder()
                   .message("Book with the same title or ISBN already exists.").success(false).build();
           return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
       }
    }

    @Override
    public ResponseEntity<?> getBookShelf(BookShelfRequest bookShelfRequest) {
        Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(existingBook).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getBookShelfs(BookShelfRequest bookShelfRequest, Pageable pageable) {
        BookCategory bookCategory=getBookCategoryById(bookShelfRequest.getId());

        Page<Book> books = iBookRepository.findByBookCategoryAndAuthorAndTitleAndIsbn(
                bookCategory,
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
            if(!bookUploadService.deleteBook(existingBook.getBookLocation())){
                BaseResponse baseResponse = BaseResponse.builder().message(FAILED_UNABLE_TO_DELETE_BOOK.getMessage())
                        .success(FAILED_UNABLE_TO_DELETE_BOOK.getSuccessful()).data(existingBook).build();
                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        iBookRepository.delete(existingBook);
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(existingBook).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);

    }

}
