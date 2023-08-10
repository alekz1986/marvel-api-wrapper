package coderio.open.pay.wrapper.api.marvel.util;

import coderio.open.pay.wrapper.api.marvel.config.properties.KeyQueryParams;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelApiException;
import coderio.open.pay.wrapper.api.marvel.exception.MarvelUnprocessableException;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

public class WebClientUtil {

    public static Function<ClientResponse, Mono<ClientResponse>> errorHandlerFunction() {
        return clientResponse -> {
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
        };
    }

    public static Function<ClientRequest, Mono<ClientRequest>> keyParamFunction(KeyQueryParams keyQueryParams) {
        return clientRequest -> {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUri(clientRequest.url());

            keyQueryParams.getKeyParams().forEach(uriBuilder::queryParam);

            ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .url(uriBuilder.build().toUri()).build();

            return Mono.just(newRequest);
        };
    }


}
