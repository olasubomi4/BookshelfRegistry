package com.group5.bookshelfregistry.exceptions;

import com.group5.bookshelfregistry.entities.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.webjars.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ControllerAdvice
@Log4j2
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException( RuntimeException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Request Could not be processed at the moment",errors);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(@NotNull AccessDeniedException ex) {

        ErrorResponse errorResponse = new ErrorResponse("Access denied Error",Arrays.asList(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);    }

    @ExceptionHandler( {DuplicateUserException.class})
    public ResponseEntity<?> handleDuplicateUserException(@NotNull DuplicateUserException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Duplicate user Error",errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({CardServiceVerificationException.class})
    public ResponseEntity<Object> handleCardServiceException(@NotNull CardServiceVerificationException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Request Could not be processed at the moment",errors);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Not Found",errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Method Not Allowed",errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Not found", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("Invalid parameter value: " + ex.getName()+" "+ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Invalid data type Error",errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler( {ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
        ErrorResponse errorResponse = new ErrorResponse("Validation Error",errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail= ex.getBody();
        String detail = Optional.ofNullable(ex.getAllValidationResults())
                .flatMap(results -> Optional.ofNullable( results).map(list ->list.get(0)))
                .map(error -> error.getResolvableErrors())
                .flatMap(error -> Optional.ofNullable(error).map(list -> list.get(0)))
                .map(message -> message.getDefaultMessage())
                .orElse("");
        problemDetail.setDetail(detail);
        return ResponseEntity.badRequest().body(problemDetail);

    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                       HttpHeaders headers, HttpStatusCode status,
                                                                       WebRequest request) {
        String paramName = ex.getParameterName();
        List<String> errors = new ArrayList<>();
        errors.add("Required parameter '" + paramName + "' is missing.");
        ErrorResponse errorResponse = new ErrorResponse("Missing parameter Error",errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        // Get the validation errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        ErrorResponse errorResponse = new ErrorResponse("Validation Error",errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Resource not found",Arrays.asList(ex.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Data integrity");
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
