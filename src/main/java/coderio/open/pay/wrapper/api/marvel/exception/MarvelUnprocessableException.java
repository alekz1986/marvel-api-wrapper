package coderio.open.pay.wrapper.api.marvel.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class MarvelUnprocessableException extends MarvelApiException {

    private final int realRawStatus;

    public MarvelUnprocessableException(int realRawStatus, ResponseEntity response) {
        super(HttpStatus.UNPROCESSABLE_ENTITY.value(), response);
        this.realRawStatus = realRawStatus;
    }

}
