package ru.vyatsu.parselib.exception;

public class ParsingRuntimeException extends RuntimeException{
    public ParsingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
