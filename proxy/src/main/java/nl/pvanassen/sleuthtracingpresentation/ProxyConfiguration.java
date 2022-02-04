package nl.pvanassen.sleuthtracingpresentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
class ProxyConfiguration {

    @Bean
    WebClient webClient(final WebClient.Builder webClientBuilder,
                        final @Value("${service.presentation.url:http://localhost:8081}") String presentationBase) {
        return webClientBuilder.baseUrl(presentationBase).build();
    }

    @Bean
    RouterFunction<ServerResponse> router(final ProxyClient proxyClient) {
        return route(GET("/"),
                req -> ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                        .body(proxyClient.getHome(), String.class))
                .andRoute(GET("/sheets/{name}"),
                        req -> ok()
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                                .body(proxyClient.getSheet(req.pathVariable("name")), String.class));
    }
}
