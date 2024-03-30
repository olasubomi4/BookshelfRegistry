package com.group5.bookshelfregistry.service;

import com.group5.bookshelfregistry.dto.bookCategory.request.BookCategoryRequest;
import com.group5.bookshelfregistry.dto.BaseResponse;
import com.group5.bookshelfregistry.dto.bookCategory.response.BookCategoryResponseData;
import com.group5.bookshelfregistry.entities.BookCategory;
import com.group5.bookshelfregistry.repositories.IBookCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.group5.bookshelfregistry.enums.ResponseDefinition.*;

@Service
@AllArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    IBookCategoryRepository iBookCategoryRepository;
    @Override
    public ResponseEntity<?> createBookCategory(BookCategoryRequest bookCategoryRequest) {
        try {
            BookCategory bookCategory = BookCategory.builder().name(bookCategoryRequest.getCategoryName()).build();
            iBookCategoryRepository.save(bookCategory);
            BookCategoryResponseData bookCategoryResponseData= BookCategoryResponseData.builder().categoryName(
                    bookCategory.getName()).id(bookCategory.getId()).build();
            BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                    SUCCESSFUL.getSuccessful()).data(bookCategoryResponseData).build();
            return ResponseEntity.ok(baseResponse);
        }catch (DataIntegrityViolationException dataIntegrityViolationException) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(BOOK_CATEGORY_ALREADY_EXIST.getMessage()).success(false).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getBookCategory(Long id) {
        BookCategory exisitingBookCategory = iBookCategoryRepository.findById(id).orElse(null);
        if(exisitingBookCategory==null) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(BOOK_CATEGORY_NOT_FOUND.getMessage()).success(BOOK_CATEGORY_NOT_FOUND.getSuccessful()).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(exisitingBookCategory).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getBookCategories(BookCategoryRequest bookCategoryRequest, Pageable pageable) {
        Page<BookCategory> bookCategories = iBookCategoryRepository.findAllByName(
                bookCategoryRequest.getCategoryName(),
                pageable);
        if(bookCategories==null||bookCategories.isEmpty()) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(BOOK_CATEGORY_NOT_FOUND.getMessage()).success(BOOK_CATEGORY_NOT_FOUND.getSuccessful()).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(bookCategories).build();
        return ResponseEntity.ok(baseResponse);

    }

    @Override
    public ResponseEntity<?> deleteBookCategory(Long id) {
        BookCategory exisitingBookCategory = iBookCategoryRepository.findById(id).orElse(null);
        if(exisitingBookCategory==null) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(BOOK_CATEGORY_NOT_FOUND.getMessage()).success(BOOK_CATEGORY_NOT_FOUND.getSuccessful()).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
        iBookCategoryRepository.delete(exisitingBookCategory);
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(exisitingBookCategory).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateBookCategory(BookCategoryRequest bookCategoryRequest) {
        BookCategory exisitingBookCategory = iBookCategoryRepository.findById(bookCategoryRequest.getId()).orElse(null);
        if(exisitingBookCategory==null) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .message(BOOK_CATEGORY_NOT_FOUND.getMessage()).success(BOOK_CATEGORY_NOT_FOUND.getSuccessful()).build();
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
        exisitingBookCategory.setName(bookCategoryRequest.getCategoryName());
        iBookCategoryRepository.save(exisitingBookCategory);
        BaseResponse baseResponse = BaseResponse.builder().message(SUCCESSFUL.getMessage()).success(
                SUCCESSFUL.getSuccessful()).data(exisitingBookCategory).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }
}
