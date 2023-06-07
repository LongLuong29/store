package com.example.store.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseObject {
    private HttpStatus status;
    private String message;
    private Object data;

    public ResponseObject(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
