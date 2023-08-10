package coderio.open.pay.wrapper.api.marvel.client.characters;

import coderio.open.pay.wrapper.api.marvel.client.characters.response.CharacterDataWrapper;
import coderio.open.pay.wrapper.api.marvel.client.characters.validator.CharacterPathValidation;
import coderio.open.pay.wrapper.api.marvel.client.characters.validator.CharactersQueryParamValidation;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CharactersClientTest {

    WebClient webClientMock;

    CharactersQueryParamValidation charactersQueryParamValidation;

    CharactersClient charactersClient;

    WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    WebClient.RequestHeadersSpec requestHeadersSpecMock;

    WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setUp() {
        this.webClientMock = mock(WebClient.class);
        this.charactersQueryParamValidation = mock(CharactersQueryParamValidation.class);
        this.requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        this.requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        this.responseSpecMock = mock(WebClient.ResponseSpec.class);
        this.charactersClient = new CharactersClient(webClientMock, charactersQueryParamValidation, "/endpoint");
    }

    @Test
    void testCallWithWebClientSuccess() {
        CharacterDataWrapper expectedData = new CharacterDataWrapper();
        Mono<CharacterDataWrapper> expectedMono = Mono.just(expectedData);
        LinkedMultiValueMap map = new LinkedMultiValueMap(Map.of(
                "key1", List.of("value1a", "value1b"),
                "key2", List.of("value2a", "value2b")));


        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(CharacterDataWrapper.class)).thenReturn(expectedMono);

        this.charactersClient.characters(map);

        assertAll(
                () -> verify(charactersQueryParamValidation).validate(any()),
                () -> verify(webClientMock).get(),
                () -> verify(requestHeadersUriSpecMock).uri(any(Function.class)),
                () -> verify(requestHeadersSpecMock).retrieve(),
                () -> verify(responseSpecMock).bodyToMono(CharacterDataWrapper.class)
        );
    }

    @Test
    void testWhenValidationThrowException() {
        LinkedMultiValueMap mapInvalid = new LinkedMultiValueMap(Map.of(
                "key1", List.of("value1a", "value1b"),
                "key2", List.of("value2a", "value2b")));

        doThrow(new MarvelBadRequestException(null))
                .when(charactersQueryParamValidation).validate(mapInvalid);

        assertThrows(MarvelBadRequestException.class,
                () -> charactersClient.characters(mapInvalid));

        assertAll(
                () -> verify(charactersQueryParamValidation).validate(mapInvalid),
                () -> verifyNoInteractions(webClientMock),
                () -> verifyNoInteractions(requestHeadersUriSpecMock),
                () -> verifyNoInteractions(requestHeadersSpecMock),
                () -> verifyNoInteractions(responseSpecMock)
        );
    }

}