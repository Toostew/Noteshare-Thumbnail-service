package com.toostew.thumbnailCore.exceptions;

public class RestControllerHandlerException extends RuntimeException {
    public RestControllerHandlerException(String message, Throwable e) {
        super(message, e);
    }
}
