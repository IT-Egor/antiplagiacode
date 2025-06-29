package ru.itegor.antiplagiacode.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final String body;
    private final int statusCode;

    public ClientException(String message, String body, int statusCode) {
        super(message);
        this.body = body;
        this.statusCode = statusCode;
    }
}
