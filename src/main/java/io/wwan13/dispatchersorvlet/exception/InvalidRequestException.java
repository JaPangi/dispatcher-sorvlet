package io.wwan13.dispatchersorvlet.exception;

public class InvalidRequestException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid Request";

    private final String message;

    public InvalidRequestException() {
        this.message = DEFAULT_MESSAGE;
    }
}
