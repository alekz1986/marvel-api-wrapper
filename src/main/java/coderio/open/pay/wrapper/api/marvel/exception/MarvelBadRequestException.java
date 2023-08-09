package coderio.open.pay.wrapper.api.marvel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class MarvelBadRequestException extends MarvelApiException {

    public MarvelBadRequestException(Map<String, Object> details) {
        super(HttpStatus.BAD_REQUEST.value(),
                new ResponseEntity(details, HttpStatus.BAD_REQUEST));
    }

}
