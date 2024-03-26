package com.group5.bookshelfregistry.exceptions;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String message){
        super(message);
    }

    public DuplicateUserException(String message, Throwable cause){
        super(message, cause);
    }
    
}