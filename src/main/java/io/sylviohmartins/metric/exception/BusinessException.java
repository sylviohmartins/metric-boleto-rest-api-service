package io.sylviohmartins.metric.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter
    private final HttpStatus httpStatus;

    public BusinessException(final String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
