package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.bookshelf.request.BookShelfRequest;
import com.group5.bookshelfregistry.dto.BaseResponse;
import com.group5.bookshelfregistry.dto.bookshelf.response.BookShelfResponseData;
import com.group5.bookshelfregistry.dto.reserveBook.ReserveBookRequest;
import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.BookCategory;
import com.group5.bookshelfregistry.entities.ReservedBook;
import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.repositories.IBookCategoryRepository;
import com.group5.bookshelfregistry.repositories.IBookRepository;
import com.group5.bookshelfregistry.repositories.IReservedBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.*;

import static com.group5.bookshelfregistry.enums.ResponseDefinition.*;

@Service
@AllArgsConstructor
public class BookShelfServiceImpl implements BookShelfService{
    IBookCategoryRepository iBookCategoryRepository;
    IBookRepository iBookRepository;
    BookUploadService bookUploadService;
    UserService userServiceImpl;
    IReservedBookRepository reservedBookRepository;
    @Override
    public ResponseEntity<?> createBookShelf(BookShelfRequest bookShelfRequest) {
        try {
            BookCategory bookCategory = getBookCategoryById(bookShelfRequest.getCategoryId());
            String bookFilesUrl = bookUploadService.upload(bookShelfRequest.getBook());
            String bookImageUrl=bookUploadService.upload(bookShelfRequest.getBookImage());
            User user= userServiceImpl.getCurrentlyLoggedInUsername().orElseThrow(() -> new NotFoundException(FAILED_TO_RETRIEVED_USER.getMessage()));
            Book book = Book.builder().bookCategory(bookCategory).author(bookShelfRequest.getAuthor()).description
                    (bookShelfRequest.getDescription()).title(bookShelfRequest.getTitle()).bookLocation(
                    bookFilesUrl).isbn(bookShelfRequest.getIsbn()).bookImageLocation(bookImageUrl)
                    .createdBy(user)
                    .build();
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
               String bookFilesUrl = bookUploadService.update(bookShelfRequest.getBook(), existingBook.getBookLocation());
               existingBook.setBookLocation(bookFilesUrl);
           }
           if (bookShelfRequest.getBookImage() != null) {
               String bookImageFileUrl = bookUploadService.update(bookShelfRequest.getBookImage(), existingBook.getBookImageLocation());
               existingBook.setBookImageLocation(bookImageFileUrl);
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
        Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(()-> new NotFoundException(BOOK_NOT_FOUND.getMessage()));
        User currentUser = userServiceImpl.getCurrentlyLoggedInUsername().orElse(null);
        if(currentUser!=null) {
            ReservedBook reservedBook = getReservedBookByUser(existingBook, currentUser);
            if(reservedBook!=null) {
                existingBook.setReservedTime(reservedBook.getReservationTime());
            }
        }
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
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(BOOK_NOT_FOUND.getMessage()).success(BOOK_NOT_FOUND.getSuccessful()).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
        User currentUser = userServiceImpl.getCurrentlyLoggedInUsername().orElse(null);
        if (currentUser != null) {
            // Fetch all reserved books for the current user
            Map<Long, ReservedBook> reservedBooksMap = getReservedBooksMapForUser(currentUser);

            // Update book information with reserved time
            for (Book book : books) {
                ReservedBook reservedBook = reservedBooksMap.get(book.getId());
                if (reservedBook != null) {
                    book.setReservedTime(reservedBook.getReservationTime());
                }
            }
        }
//        List<BookShelfResponseData > bookShelfResponseDataList= new ArrayList<>();
//        if(currentUser!=null) {
//            for (Book book : books) {
//                ReservedBook reservedBook = getReservedBookByUser(book, currentUser);
//                if(reservedBook!=null) {
//                    book.setReservedTime(reservedBook.getReservationTime());
////                    BookShelfResponseData bookShelfResponseData = convertToBookShelfResponseData(book, reservedBook);
////                    bookShelfResponseDataList.add(bookShelfResponseData);
//                }
//            }
//        }
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(books).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);

    }
    private ReservedBook getReservedBookByUser(Book book, User user) {
        ReservedBook reservedBook = reservedBookRepository.findByUserAndBook(user,book);
        return reservedBook;
    }

    private Map<Long, ReservedBook> getReservedBooksMapForUser(User user) {
        // Fetch all reserved books for the current user in a single query
        List<ReservedBook> reservedBooks = reservedBookRepository.findByUser(user);
        Map<Long, ReservedBook> reservedBooksMap = new HashMap<>();
        for (ReservedBook reservedBook : reservedBooks) {
            reservedBooksMap.put(reservedBook.getBook().getId(), reservedBook);
        }
        return reservedBooksMap;
    }

    @Override
    public ResponseEntity<?> getReservedBooks(Pageable pageable) {
        User currentUser = userServiceImpl.getCurrentlyLoggedInUsername().orElseThrow(() ->
                new NotFoundException(FAILED_TO_RETRIEVED_USER.getMessage()));
        Page<ReservedBook> reservedBooks = reservedBookRepository.findAllByUser(currentUser,pageable);
        if(reservedBooks.isEmpty()) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(RESERVED_BOOKS_NOT_FOUND.getMessage()).success(RESERVED_BOOKS_NOT_FOUND.getSuccessful()).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(reservedBooks).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
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
        Book existingBook = iBookRepository.findById(bookShelfRequest.getId()).orElseThrow(()-> new NotFoundException(BOOK_NOT_FOUND.getMessage()));
        User currentUser = userServiceImpl.getCurrentlyLoggedInUsername().orElseThrow(() ->
                new NotFoundException(FAILED_TO_RETRIEVED_USER.getMessage()));
        if (existingBook.getBookLocation() != null) {
            if(!bookUploadService.delete(existingBook.getBookLocation())){
                BaseResponse baseResponse = BaseResponse.builder().message(FAILED_UNABLE_TO_DELETE_BOOK.getMessage())
                        .success(FAILED_UNABLE_TO_DELETE_BOOK.getSuccessful()).data(existingBook).build();
                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (existingBook.getBookImageLocation() != null) {
            if(!bookUploadService.delete(existingBook.getBookImageLocation())){
                BaseResponse baseResponse = BaseResponse.builder().message(FAILED_UNABLE_TO_DELETE_BOOK.getMessage())
                        .success(FAILED_UNABLE_TO_DELETE_BOOK.getSuccessful()).data(existingBook).build();
                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        existingBook.setDeletedBy(currentUser);
        iBookRepository.delete(existingBook);
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(existingBook).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> reserveBook(ReserveBookRequest reserveBookRequest) {
            Book existingBook = iBookRepository.findById(reserveBookRequest.getBookId()).orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND.getMessage()));
            User currentUser = userServiceImpl.getCurrentlyLoggedInUsername().orElseThrow(() -> new NotFoundException(FAILED_TO_RETRIEVED_USER.getMessage()));

            ReservedBook existingReservedBook= reservedBookRepository.findByUserAndBook(currentUser,existingBook);
            if(existingReservedBook!=null) {
                BaseResponse baseResponse = BaseResponse.builder()
                        .message(FAILED_TO_RESERVE_ALREADY_RESERVED_BOOK.getMessage()).success(FAILED_TO_RESERVE_ALREADY_RESERVED_BOOK.getSuccessful()).build();
                return new ResponseEntity<>(baseResponse, HttpStatus.OK);
            }
            ReservedBook reservedBook = ReservedBook.builder().book(existingBook).user(currentUser).build();
            reservedBookRepository.save(reservedBook);
            BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFULLY_RESERVED_BOOK.getMessage()).success(
                    SUCCESSFULLY_RESERVED_BOOK.getSuccessful()).data(reservedBook).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
