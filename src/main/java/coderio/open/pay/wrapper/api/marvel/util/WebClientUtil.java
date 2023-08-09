package coderio.open.pay.wrapper.api.marvel.util;

import coderio.open.pay.wrapper.api.marvel.config.properties.KeyQueryParams;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelApiException;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelUnprocessableException;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

public class WebClientUtil {

    public static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            int rawStatus = clientResponse.rawStatusCode();

            if (rawStatus>=500 && rawStatus<=599) {
                return clientResponse.toEntity(Map.class)
                        .flatMap(data -> Mono.error(new MarvelUnprocessableException(rawStatus, data)));
            }

            if(rawStatus < 200 || rawStatus > 299) {
                return clientResponse.toEntity(Map.class)
                        .flatMap(data -> Mono.error(new MarvelApiException(clientResponse.rawStatusCode(), data)));
            }

            return Mono.just(clientResponse);
        });
    }

    public static ExchangeFilterFunction keyParams(KeyQueryParams keyQueryParams) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUri(clientRequest.url());

            keyQueryParams.getKeyParams().forEach(uriBuilder::queryParam);

            ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .url(uriBuilder.build().toUri()).build();

            return Mono.just(newRequest);
        });

    }


}
