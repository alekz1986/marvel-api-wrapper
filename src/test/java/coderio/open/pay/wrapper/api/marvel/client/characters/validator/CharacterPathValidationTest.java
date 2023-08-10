package coderio.open.pay.wrapper.api.marvel.client.characters.validator;

import coderio.open.pay.wrapper.api.marvel.exception.MarvelBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CharacterPathValidationTest {

    @InjectMocks
    CharacterPathValidation characterPathValidation;

    @Test
    void testWhenDataIsSuccess() {
        String params = "any-info";

        assertDoesNotThrow(() -> this.characterPathValidation.validate(params));
    }


    @ParameterizedTest
    @NullAndEmptySource
    void testWhenDataIsInvalid(String value) {
        assertThrows(MarvelBadRequestException.class,
                () -> this.characterPathValidation.validate(value));
    }

}