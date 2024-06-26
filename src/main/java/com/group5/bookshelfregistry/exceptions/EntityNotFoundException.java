package com.group5.bookshelfregistry.exceptions;

public class EntityNotFoundException extends RuntimeException { 

    public EntityNotFoundException( Class<?> entity) {
            super("The " + entity.getSimpleName().toLowerCase() + "' does not exist in our records");
    }

}