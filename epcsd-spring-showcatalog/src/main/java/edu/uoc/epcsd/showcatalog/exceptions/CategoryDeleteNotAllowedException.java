package edu.uoc.epcsd.showcatalog.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "category can not be deleted")
public class CategoryDeleteNotAllowedException extends RuntimeException {
    public CategoryDeleteNotAllowedException(String message) {
        log.error("Category can not be deleted: {}", message);
    }
}
