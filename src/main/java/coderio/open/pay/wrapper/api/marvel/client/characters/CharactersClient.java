package coderio.open.pay.wrapper.api.marvel.client.characters;

import coderio.open.pay.wrapper.api.marvel.client.BaseClient;
import coderio.open.pay.wrapper.api.marvel.client.characters.response.CharacterDataWrapper;
import coderio.open.pay.wrapper.api.marvel.client.characters.validator.CharactersQueryParamValidation;
import coderio.open.pay.wrapper.api.marvel.util.IValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CharactersClient extends BaseClient {

    private final String endpoint;

    private final IValidation<MultiValueMap<String, String>> charactersQueryParamValidation;

    public CharactersClient(WebClient marvelWebClient,
            CharactersQueryParamValidation charactersQueryParamValidation,
            @Value("${api-marvel.wrapper.endpoints.characters.path}") String endpoint) {
        super(marvelWebClient);
        this.charactersQueryParamValidation = charactersQueryParamValidation;
        this.endpoint = endpoint;
    }

    public Mono<CharacterDataWrapper> characters(MultiValueMap<String, String> params) {

        charactersQueryParamValidation.validate(params);

        return this.getMarvelWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(this.endpoint)
                        .queryParams(params)
                        .build())
                .retrieve()
                .bodyToMono(CharacterDataWrapper.class);
    }



}
