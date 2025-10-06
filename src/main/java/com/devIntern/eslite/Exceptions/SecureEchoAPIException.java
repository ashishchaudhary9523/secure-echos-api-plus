package com.devIntern.eslite.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class SecureEchoAPIException extends RuntimeException {
    @Getter
    private HttpStatus status;
    private String name;

    public SecureEchoAPIException(HttpStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    public SecureEchoAPIException(HttpStatus status , String message1 , String message2){
        super(message1);
        this.status = status;
        this.name = message2;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
