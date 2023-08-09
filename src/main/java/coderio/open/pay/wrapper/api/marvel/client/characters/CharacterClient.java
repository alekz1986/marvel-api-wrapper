package coderio.open.pay.wrapper.api.marvel.client.characters;

import coderio.open.pay.wrapper.api.marvel.client.BaseClient;
import coderio.open.pay.wrapper.api.marvel.client.characters.response.CharacterDataWrapper;
import coderio.open.pay.wrapper.api.marvel.client.characters.validator.CharacterPathValidation;
import coderio.open.pay.wrapper.api.marvel.util.IValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CharacterClient extends BaseClient {

    private final String endpoint;

    private final String characterIdName;

    private final IValidation<String> characterPathValidation;

    public CharacterClient(WebClient marvelWebClient,
            CharacterPathValidation characterPathValidation,
            @Value("${api-marvel.wrapper.endpoints.character.path}") String endpoint) {
        super(marvelWebClient);
        this.endpoint = endpoint;
        this.characterPathValidation = characterPathValidation;
        this.characterIdName = "characterId";
    }

    public Mono<CharacterDataWrapper> characters(String characterId) {

        characterPathValidation.validate(characterId);

        return this.getMarvelWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(this.endpoint)
                        .build(characterId))
                .retrieve()
                .bodyToMono(CharacterDataWrapper.class);
    }

}
