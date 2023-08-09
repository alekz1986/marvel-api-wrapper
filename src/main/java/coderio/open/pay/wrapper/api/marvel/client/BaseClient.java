package coderio.open.pay.wrapper.api.marvel.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Getter
@AllArgsConstructor
public class BaseClient {

    private final WebClient marvelWebClient;

}
