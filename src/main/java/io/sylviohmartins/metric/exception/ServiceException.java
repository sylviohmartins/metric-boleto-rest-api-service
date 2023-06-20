package io.sylviohmartins.metric.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ServiceException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public ServiceException(final String message, final HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
