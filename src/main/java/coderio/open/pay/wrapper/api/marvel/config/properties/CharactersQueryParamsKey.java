package coderio.open.pay.wrapper.api.marvel.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "api-marvel.wrapper.endpoints.characters")
@Getter
@Setter
public class CharactersQueryParamsKey {

    private List<String> queryParamsKey;

}
