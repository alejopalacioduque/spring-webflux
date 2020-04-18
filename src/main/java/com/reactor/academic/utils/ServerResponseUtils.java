package com.reactor.academic.utils;

import com.reactor.academic.service.ICRUD;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public class ServerResponseUtils {

    private static String ID_EXIST = "%s already exist";

    public static <T, V> Mono<ServerResponse> getCreateResponse(Mono<T> mono, ICRUD<T, V> service, T t, String uri) {
        return mono
                .flatMap(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .body(BodyInserters.fromValue(String.format(ID_EXIST, t.getClass().getCanonicalName()))))
                .switchIfEmpty(service.register(t)
                        .flatMap(p -> ServerResponse.created(URI.create(uri))
                                .contentType(MediaType.APPLICATION_STREAM_JSON)
                                .body(fromValue(p))));
    }
}
