package edu.uoc.epcsd.showcatalog.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "show not found")
public class ShowNotFoundException extends RuntimeException {
    public ShowNotFoundException(String message) {
        log.error("Show not found: {}", message);
    }
}