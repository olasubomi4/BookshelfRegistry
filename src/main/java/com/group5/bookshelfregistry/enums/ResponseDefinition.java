package com.group5.bookshelfregistry.enums;
public enum ResponseDefinition {
    SUCCESSFUL("successful",true),
    FAILED_UNABLE_TO_DELETE_BOOK("Unable to delete book, Please try again later.",false),
    BOOK_NOT_FOUND("Book doesnt exist on our system",false),
    FAILED("FAILED",true);
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
