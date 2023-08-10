package coderio.open.pay.wrapper.api.marvel.client.characters;

import coderio.open.pay.wrapper.api.marvel.client.characters.response.CharacterDataWrapper;
import coderio.open.pay.wrapper.api.marvel.client.characters.validator.CharacterPathValidation;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelBadRequestException;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelUnprocessableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.CharacterData;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CharacterClientTest {

    WebClient webClientMock;

    CharacterPathValidation characterPathValidation;

    CharacterClient characterClient;

    WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    WebClient.RequestHeadersSpec requestHeadersSpecMock;

    WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setUp() {
        this.webClientMock = mock(WebClient.class);
        this.characterPathValidation = mock(CharacterPathValidation.class);
        this.requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        this.requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        this.responseSpecMock = mock(WebClient.ResponseSpec.class);
        this.characterClient = new CharacterClient(
                this.webClientMock,
                this.characterPathValidation,
                "/endpoint");
    }

    @Test
    void testCallWithWebClientSuccess() {
        CharacterDataWrapper expectedData = new CharacterDataWrapper();
        Mono<CharacterDataWrapper> expectedMono = Mono.just(expectedData);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(CharacterDataWrapper.class)).thenReturn(expectedMono);

        this.characterClient.characters("characterId");

        assertAll(
                () -> verify(characterPathValidation).validate(any(String.class)),
                () -> verify(webClientMock).get(),
                () -> verify(requestHeadersUriSpecMock).uri(any(Function.class)),
                () -> verify(requestHeadersSpecMock).retrieve(),
                () -> verify(responseSpecMock).bodyToMono(CharacterDataWrapper.class)
        );
    }

    @Test
    void testWhenValidationThrowException() {
        String invalidCharacterId = "invalid";

        doThrow(new MarvelBadRequestException(null))
                .when(characterPathValidation).validate(invalidCharacterId);

        assertThrows(MarvelBadRequestException.class,
                () -> characterClient.characters(invalidCharacterId));

        assertAll(
                () -> verify(characterPathValidation).validate(invalidCharacterId),
                () -> verifyNoInteractions(webClientMock),
                () -> verifyNoInteractions(requestHeadersUriSpecMock),
                () -> verifyNoInteractions(requestHeadersSpecMock),
                () -> verifyNoInteractions(responseSpecMock)
        );

    }


}