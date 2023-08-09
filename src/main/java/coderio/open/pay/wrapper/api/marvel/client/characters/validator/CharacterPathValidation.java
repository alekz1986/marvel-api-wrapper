package coderio.open.pay.wrapper.api.marvel.client.characters.validator;

import coderio.open.pay.wrapper.api.marvel.exception.MarvelApiException;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelBadRequestException;
import coderio.open.pay.wrapper.api.marvel.util.IValidation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class CharacterPathValidation implements IValidation<String> {

    @Override
    public void validate(String params) throws MarvelApiException {
        if (!StringUtils.hasLength(params)) {
            throw new MarvelBadRequestException(Map.of("message",
                    "The parameter cannot be empty or null."));
        }
    }

}
