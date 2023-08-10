package coderio.open.pay.wrapper.api.marvel.client.characters.validator;

import coderio.open.pay.wrapper.api.marvel.config.properties.CharactersQueryParamsKey;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharactersQueryParamValidationTest {

    @InjectMocks
    CharactersQueryParamValidation charactersQueryParamValidation;

    @Mock
    CharactersQueryParamsKey paramsQueryKey;


    @ParameterizedTest
    @MethodSource("argumentsDataValid")
    void testWhenDataForValidateHasValues(MultiValueMap<String, String> values,
            List<String> keyAllowed) {

        when(paramsQueryKey.getQueryParamsKey()).thenReturn(keyAllowed);

        this.charactersQueryParamValidation.validate(values);

        assertDoesNotThrow(() -> this.charactersQueryParamValidation.validate(values));
    }

    @ParameterizedTest
    @MethodSource("argumentsDataInvalid")
    void testWhenDataForValidateIsInvalid(MultiValueMap<String, String> values,
            List<String> keyAllowed) {

        when(paramsQueryKey.getQueryParamsKey()).thenReturn(keyAllowed);

        assertThrows(MarvelBadRequestException.class,
                () -> this.charactersQueryParamValidation.validate(values));
    }

    static Stream<Arguments> argumentsDataInvalid() {
        MultiValueMap value = new LinkedMultiValueMap(Map.of(
                "key1", List.of("value1a", "value1b")));
        MultiValueMap values = new LinkedMultiValueMap(Map.of(
                "key1", List.of("value1a", "value1b"),
                "key2", List.of("value2a", "value2b")));
        MultiValueMap emptyValues = new LinkedMultiValueMap(Map.of(
                "key1", List.of("", ""),
                "key2", List.of("value2a", "value2b")));
        return Stream.of(
                Arguments.of(value, List.of()),
                Arguments.of(value, List.of("keyX")),
                Arguments.of(value, List.of("keyX", "keyY")),
                Arguments.of(values, List.of()),
                Arguments.of(values, List.of("key1", "keyX")),
                Arguments.of(values, List.of("key2", "keyX")),
                Arguments.of(values, List.of("keyX", "keyY")),
                Arguments.of(emptyValues, List.of()),
                Arguments.of(emptyValues, List.of("key1", "key2")),
                Arguments.of(emptyValues, List.of("key1", "keyX")),
                Arguments.of(emptyValues, List.of("key1", "keyX", "KeyY"))
        );
    }

    static Stream<Arguments> argumentsDataValid() {
        MultiValueMap value = new LinkedMultiValueMap(Map.of(
                "key1", List.of("value1a", "value1b")));
        MultiValueMap values = new LinkedMultiValueMap(Map.of(
                "key1", List.of("value1a", "value1b"),
                "key2", List.of("value2a", "value2b")));
        return Stream.of(
                Arguments.of(value, List.of("key1")),
                Arguments.of(value, List.of("key1", "key2")),
                Arguments.of(value, List.of("key0", "key1", "key2")),
                Arguments.of(values, List.of("key1", "key2")),
                Arguments.of(values, List.of("key1", "key2", "key3")),
                Arguments.of(values, List.of("key0", "key1", "key2", "key3"))
        );
    }
}