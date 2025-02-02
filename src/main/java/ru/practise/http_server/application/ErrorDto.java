package ru.practise.http_server.application;

public class ErrorDto {
    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorDto(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
