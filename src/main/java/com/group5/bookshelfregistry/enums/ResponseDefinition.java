package com.group5.bookshelfregistry.enums;
public enum ResponseDefinition {
    SUCCESSFUL("Successful",true),
    FAILED_UNABLE_TO_DELETE_BOOK("Unable to delete book, Please try again later.",false),
    BOOK_NOT_FOUND("Book doesnt exist on our system",false),
    BOOK_CATEGORY_NOT_FOUND("Book category doesnt exist on our system",false),
    FAILED("Failed",false),

    BOOK_CATEGORY_ALREADY_EXIST("Category name already exists.",false),
    UNABLE_TO_UPDATE_USER_READING_PROGRESS("Unable to update user reading progress",false),

    UNABLE_TO_RETRIEVE_USER_READING_PROGRESS("Unable to retrieve user reading progress",false),

    SUCCESSFULLY_RETRIEVED_USER("Successfully retrieved user details",true),
    FAILED_TO_RETRIEVED_USER("Failed to retrieve user details",false),
    SUCCESSFULLY_RESERVED_BOOK("Successfully reserved book",true),
    FAILED_TO_RESERVED_BOOK("Failed to reserve book",false),

    FAILED_TO_RESERVE_ALREADY_RESERVED_BOOK("Failed to reserve already reserved book",false),

    RESERVED_BOOKS_NOT_FOUND("Unable to find books reserved by the current user",false),

    USER_ALREADY_EXIST("User already exist",false);
    private String message;
    private Boolean successful;

    ResponseDefinition(String message, Boolean successful) {
        this.message = message;
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccessful() {
        return successful;
    }
}
