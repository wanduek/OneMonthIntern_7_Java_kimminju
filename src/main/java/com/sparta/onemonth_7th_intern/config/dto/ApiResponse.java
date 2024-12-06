package com.sparta.onemonth_7th_intern.config.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private enum Status {
        SUCCESS, FAIL, ERROR
    }

    private final Status status;
    private final T data;
    private final String message;

    private ApiResponse(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
