package org.example.exceptions;

public class ParsingRuntimeException extends RuntimeException{
    public ParsingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
