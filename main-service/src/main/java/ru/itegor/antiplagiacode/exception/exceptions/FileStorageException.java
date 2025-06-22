package ru.itegor.antiplagiacode.exception.exceptions;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message, Exception exception) {
        super(message, exception);
    }
}
