package com.nitrowise.basesrvr.exception.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionJson {

    String status;
    String message;
    String developerMessage;

    public ExceptionJson(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ExceptionJson(String message) {
        this.message = message;
    }

}
