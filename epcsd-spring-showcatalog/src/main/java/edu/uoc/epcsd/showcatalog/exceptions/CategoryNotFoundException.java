package edu.uoc.epcsd.showcatalog.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "category not found")
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        log.error("Category not found: {}", message);
    }
}
