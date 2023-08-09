package coderio.open.pay.wrapper.api.marvel.config;

import coderio.open.pay.wrapper.api.marvel.config.properties.KeyQueryParams;
import coderio.open.pay.wrapper.api.marvel.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient marvelWebClient(WebClient.Builder webClientBuilder,
            KeyQueryParams keyQueryParams,
            @Value("${api-marvel.wrapper.base}") String base) {
        return webClientBuilder
                .baseUrl(base)
                .filter(WebClientUtil.keyParams(keyQueryParams))
                .filter(WebClientUtil.errorHandler())
                .build();
    }

}
