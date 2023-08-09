package coderio.open.pay.wrapper.api.marvel.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "api-marvel.wrapper")
@Getter
@Setter
public class KeyQueryParams {

    private Map<String, String> keyParams;

}
