package edu.uoc.epcsd.showcatalog.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "category already exists")
public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String message) {
        log.error("Category already exists: {}", message);
    }
}
