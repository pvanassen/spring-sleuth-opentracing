package nl.pvanassen.sleuthtracingpresentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
record ProxyClient(WebClient webClient) {

    Mono<String> getHome() {
        log.info("Proxy to home");
        return webClient.get().uri("/").exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class));
    }


    Mono<String> getSheet(final String name) {
        log.info("Proxy to {}", name);
        return webClient.get().uri("/sheets/" + name).exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class));
    }
}
