package br.com.erudio.erudioapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectNullException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RequiredObjectNullException(String message) {
        super(message);
    }
    public RequiredObjectNullException() {

        super("Persisting null object is not allowed");
    }

}
