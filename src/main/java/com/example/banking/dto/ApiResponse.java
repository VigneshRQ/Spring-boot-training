package com.example.banking.dto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;


@Data
@RequiredArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<List<T>> successList(String message, List<T> items) {
        return new ApiResponse<>(true, message, items);
    }

    // Optional: Specific method for empty lists
    public static <T> ApiResponse<List<T>> successEmptyList(String message) {
        return new ApiResponse<>(true, message, Collections.emptyList());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    // Overloading method to accept both message and data
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // Overloading method to accept both message and data
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
