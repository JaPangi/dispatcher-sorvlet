package io.wwan13.dispatchersorvlet.exception;

public class ResolveArgumentException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Cannot resolve argument without annotations";

    private final String message;

    public ResolveArgumentException() {
        this.message = DEFAULT_MESSAGE;
    }
}
