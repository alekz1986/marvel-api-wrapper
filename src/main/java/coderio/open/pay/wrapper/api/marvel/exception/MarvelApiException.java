package coderio.open.pay.wrapper.api.marvel.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@Getter

public class MarvelApiException extends ResponseStatusException {

    private final ResponseEntity response;

    public MarvelApiException(int rawStatusCode, ResponseEntity response) {
        super(rawStatusCode, null, null);
        this.response = response;
    }

}
