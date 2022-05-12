package edu.uoc.epcsd.showcatalog.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "show update not allowed")
public class ShowUpdateNotAllowedException extends RuntimeException {
    public ShowUpdateNotAllowedException(String message) {
        log.error("Show update not allowed: {}", message);
    }
}
