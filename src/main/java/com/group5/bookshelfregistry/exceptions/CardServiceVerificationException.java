package com.group5.bookshelfregistry.exceptions;

public class CardServiceVerificationException extends RuntimeException {

    public CardServiceVerificationException(String message){
        super(message);
    }

    public CardServiceVerificationException(String message, Throwable cause){
        super(message, cause);
    }
    
}