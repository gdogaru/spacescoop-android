package com.gdogaru.spacescoop.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApiResponse<T> {
    private Status status;
    private T value;

    public enum Status {SUCCESS, ERROR}
}
