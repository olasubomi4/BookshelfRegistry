package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.BaseResponse;
import com.group5.bookshelfregistry.dto.readingProgress.UpdateReadingProgressRequest;
import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.ReadingProgress;
import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.repositories.IBookRepository;
import com.group5.bookshelfregistry.repositories.IReadingProgressRepository;
import com.group5.bookshelfregistry.security.utill.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import static com.group5.bookshelfregistry.enums.ResponseDefinition.*;

@Service
public class ReadingProgressServiceImpl implements ReadingProgressService {

    @Autowired
    IReadingProgressRepository iReadingProgressRepository;
    @Autowired
    private SecurityUtility securityUtility;

    @Autowired
    private IBookRepository iBookRepository;
    @Override
    public ResponseEntity<?> updateReadingProgress(UpdateReadingProgressRequest updateReadingProgressRequest) {
        User user= securityUtility.getCurrentlyLoggedInUsername().orElseThrow( ()->new RuntimeException(
                UNABLE_TO_UPDATE_USER_READING_PROGRESS.getMessage()));
        Book book=iBookRepository.findById(updateReadingProgressRequest.getBookId()).orElseThrow(() -> new NotFoundException(
                BOOK_NOT_FOUND.getMessage()));

        ReadingProgress readingProgress= ReadingProgress.builder().currentPage(updateReadingProgressRequest
                .getCurrentPage()).book(book).user(user).build();
        iReadingProgressRepository.save(readingProgress);

        BaseResponse baseResponse= BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(true).
                data(readingProgress).build();

        return ResponseEntity.ok(baseResponse);

    }

    @Override
    public ResponseEntity<?> getReadingProgress(Long bookId) {
        User user= securityUtility.getCurrentlyLoggedInUsername().orElseThrow( () ->new RuntimeException(
                UNABLE_TO_RETRIEVE_USER_READING_PROGRESS.getMessage()));
        Book book=iBookRepository.findById(bookId).orElseThrow(() -> new RuntimeException(
                BOOK_NOT_FOUND.getMessage()));

        ReadingProgress readingProgress=iReadingProgressRepository.findFirstByBookAndUser(book,user).orElseThrow( () ->new RuntimeException(
                UNABLE_TO_RETRIEVE_USER_READING_PROGRESS.getMessage()));

        BaseResponse baseResponse= BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(true).
                data(readingProgress).build();
        return ResponseEntity.ok(baseResponse);
    }

    @Override
    public ResponseEntity<?> getAllReadingProgress(String bookTitle, Pageable pageable) {
        User user= securityUtility.getCurrentlyLoggedInUsername().orElseThrow( () ->new RuntimeException(
                UNABLE_TO_RETRIEVE_USER_READING_PROGRESS.getMessage()));
        Book book=iBookRepository.findFirstByTitle(bookTitle).orElseThrow(() -> new RuntimeException(
                BOOK_NOT_FOUND.getMessage()));
        Page<ReadingProgress> readingProgresses;

        if(bookTitle==null) {
            readingProgresses = iReadingProgressRepository.findAllByBookAndUser(book,user,pageable).orElseThrow( () ->new RuntimeException(
                    UNABLE_TO_RETRIEVE_USER_READING_PROGRESS.getMessage()));
        }
        else {
            readingProgresses=iReadingProgressRepository.findAllByUser(user,pageable).orElseThrow( () ->new RuntimeException(
                    UNABLE_TO_RETRIEVE_USER_READING_PROGRESS.getMessage()));
        }

        BaseResponse baseResponse= BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(true).
                data(readingProgresses).build();
        return ResponseEntity.ok(baseResponse);
    }
}
