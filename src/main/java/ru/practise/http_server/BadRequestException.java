package ru.practise.http_server;

public class BadRequestException extends RuntimeException {
    private final String code;
    private final String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BadRequestException(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
