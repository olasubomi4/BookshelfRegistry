package com.group5.bookshelfregistry.exceptions;

import com.group5.bookshelfregistry.dto.BaseResponse;
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
import java.util.Optional;


@ControllerAdvice
@Log4j2
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException( RuntimeException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Request Could not be processed at the moment");
//        BaseResponse baseResponse= BaseResponse.builder().data(problemDetail).success(false).message().build();
        return ResponseEntity.internalServerError().body(problemDetail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(@NotNull AccessDeniedException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle("Access denied Error");
        return new ResponseEntity<>(problemDetail, HttpStatus.FORBIDDEN);    }

    @ExceptionHandler( {DuplicateUserException.class})
    public ResponseEntity<?> handleDuplicateUserException(@NotNull DuplicateUserException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Duplicate user Error");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler({CardServiceVerificationException.class})
    public ResponseEntity<Object> handleCardServiceException(@NotNull CardServiceVerificationException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Request Could not be processed at the moment");
        return ResponseEntity.internalServerError().body(problemDetail);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Not Found");
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
        problemDetail.setTitle("Method Not Allowed");
        return new ResponseEntity<>(problemDetail, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Not found");
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage="Invalid parameter value: " + ex.getName()+" "+ex.getMessage();
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setTitle("Invalid data type Error");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler( {ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        String errorMessage=ex.getConstraintViolations().stream().findFirst().map(cv->cv.getMessage()).orElse(null);
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setTitle("Validation Error");
        return ResponseEntity.badRequest().body(problemDetail);
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
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Required parameter '" + paramName + "' is missing.");

        problemDetail.setTitle("Missing parameter Error");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        // Get the validation errors
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream().findFirst()
                .map(FieldError::getDefaultMessage)
                .get();

        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setTitle("Validation Error");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(RuntimeException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("resource not found");
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Data integrity");
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
