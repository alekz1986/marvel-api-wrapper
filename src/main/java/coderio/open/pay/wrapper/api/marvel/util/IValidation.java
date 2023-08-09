package coderio.open.pay.wrapper.api.marvel.util;

import coderio.open.pay.wrapper.api.marvel.exception.MarvelApiException;

public interface IValidation<T> {

    void validate(T params) throws MarvelApiException;

}
