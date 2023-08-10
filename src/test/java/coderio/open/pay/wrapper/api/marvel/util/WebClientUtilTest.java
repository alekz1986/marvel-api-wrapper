package coderio.open.pay.wrapper.api.marvel.util;

import coderio.open.pay.wrapper.api.marvel.config.properties.KeyQueryParams;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelApiException;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelUnprocessableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WebClientUtilTest {

    @ParameterizedTest
    @ValueSource(ints = {200, 201, 202, 203, 204, 205, 250, 299})
    void testErrorHandlerWhenRawStatusIsSuccess(int rawStatusCode) {
        Function<ClientResponse, Mono<ClientResponse>> function = WebClientUtil.errorHandlerFunction();
        ClientResponse clientResponse = ClientResponse
                .create(rawStatusCode, ExchangeStrategies.withDefaults())
                .build();

        ClientResponse response = function.apply(clientResponse).block();

        assertEquals(clientResponse, response);
    }

    @ParameterizedTest
    @MethodSource("statusParams")
    void testErrorHandlerWhenRawStatusIsNotSuccessThrowException(int rawStatusCode, Class<? extends MarvelApiException> expected) {
        Function<ClientResponse, Mono<ClientResponse>> function = WebClientUtil.errorHandlerFunction();
        ClientResponse clientResponse = ClientResponse
                .create(rawStatusCode, ExchangeStrategies.withDefaults())
                .build();

        MarvelApiException exception = assertThrows(expected,
                () -> function.apply(clientResponse).block());

        assertAll(
                () -> assertEquals(expected, exception.getClass()),
                () -> assertEquals(rawStatusCode, exception.getResponse().getStatusCodeValue()),
                () -> { if (exception instanceof MarvelUnprocessableException) {
                    MarvelUnprocessableException ex = (MarvelUnprocessableException) exception;
                    assertEquals(ex.getRealRawStatus(), rawStatusCode);
                    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getRawStatusCode());
                }}

        );
    }


    @Test
    void testKeyParamFunction() throws URISyntaxException {
        LinkedHashMap<String, String> queryParams = new LinkedHashMap() {{
                put("key1", "value1");
                put("key2", "value2");
            }};
        String expected = queryParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        KeyQueryParams params = new KeyQueryParams();
        params.setKeyParams(queryParams);
        Function<ClientRequest, Mono<ClientRequest>> function = WebClientUtil.keyParamFunction(params);
        ClientRequest clientRequest = ClientRequest.create(HttpMethod.GET, new URI("http://mock")).build();

        ClientRequest resultRequest = function.apply(clientRequest).block();

        assertEquals(expected, resultRequest.url().getQuery());
    }

    static Stream<Arguments> statusParams() {
        return Stream.of(
                Arguments.of(500, MarvelUnprocessableException.class),
                Arguments.of(501, MarvelUnprocessableException.class),
                Arguments.of(502, MarvelUnprocessableException.class),
                Arguments.of(503, MarvelUnprocessableException.class),
                Arguments.of(504, MarvelUnprocessableException.class),
                Arguments.of(505, MarvelUnprocessableException.class),
                Arguments.of(580, MarvelUnprocessableException.class),
                Arguments.of(599, MarvelUnprocessableException.class),
                Arguments.of(100, MarvelApiException.class),
                Arguments.of(150, MarvelApiException.class),
                Arguments.of(199, MarvelApiException.class),
                Arguments.of(300, MarvelApiException.class),
                Arguments.of(350, MarvelApiException.class),
                Arguments.of(399, MarvelApiException.class),
                Arguments.of(400, MarvelApiException.class),
                Arguments.of(450, MarvelApiException.class),
                Arguments.of(499, MarvelApiException.class)
        );
    }



}