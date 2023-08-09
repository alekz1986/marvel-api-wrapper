package coderio.open.pay.wrapper.api.marvel.client.characters.validator;

import coderio.open.pay.wrapper.api.marvel.config.properties.CharactersQueryParamsKey;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelApiException;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelBadRequestException;
import coderio.open.pay.wrapper.api.marvel.util.IValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CharactersQueryParamValidation implements IValidation<MultiValueMap<String, String>> {


    private final CharactersQueryParamsKey paramsQueryKey;

    @Override
    public void validate(MultiValueMap<String, String> params) throws MarvelApiException {
        if (params == null || params.isEmpty()) {
            return;
        }

        List<String> notAllowed = new ArrayList<>();
        List<String> noData = new ArrayList<>();

        params.toSingleValueMap().forEach((key, value) -> {
            if (isKeyNotAllowed(key)) {
                notAllowed.add(key);
                return;
            }
            if(isValueEmpty(value)) {
                noData.add(key);
            }
        });

        if (hasError(notAllowed, noData)) {
            throw new MarvelBadRequestException(buildErrorDetails(notAllowed, noData));
        }
    }

    private boolean isKeyNotAllowed(String key) {
        return !paramsQueryKey.getQueryParamsKey().contains(key);
    }

    private boolean isValueEmpty(String value) {
        return !StringUtils.hasLength(value);
    }

    private boolean hasError(List<String> notAllowed, List<String> noData) {
        return !notAllowed.isEmpty() || !noData.isEmpty();
    }

    private Map<String, Object> buildErrorDetails(List<String> notAllowed, List<String> noData) {
        if (notAllowed.isEmpty() && noData.isEmpty()) {
            return null;
        }

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("message", "Invalid query params");

        if (!notAllowed.isEmpty()) {
            error.put("not-allowed", new LinkedHashMap<>() {{
                    put("message", "The following parameters are not allowed");
                    put("query-params", notAllowed);
            }});
        }

        if (!noData.isEmpty()) {
            error.put("empty", new LinkedHashMap<>() {{
                    put("message", "The following parameters cannot be empty or null.");
                    put("no-data", noData);
            }});
        }

        return error;
    }


}
