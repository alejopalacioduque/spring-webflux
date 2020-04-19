package com.reactor.academic.handler;

import com.reactor.academic.documment.Course;
import com.reactor.academic.service.ICourseService;
import com.reactor.academic.validators.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class CourseHandler {


    private ICourseService service;

    private RequestValidator generalValidator;

    @Autowired
    public CourseHandler(ICourseService service, RequestValidator generalValidator) {
        this.service = service;
        this.generalValidator = generalValidator;
    }

    public Mono<ServerResponse> list(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(service.list(), Course.class);
    }

    public Mono<ServerResponse> listById(ServerRequest req) {
        Optional<String> id = req.queryParam("idCourse");

        return service.listById(id.get())
                .flatMap(p -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(fromValue(p))
                )
                .switchIfEmpty(ServerResponse
                        .notFound()
                        .build()
                );

    }

    public Mono<ServerResponse> register(ServerRequest req) {
        Mono<Course> courseMono = req.bodyToMono(Course.class);

        return courseMono
                .flatMap(this.generalValidator::validate)
                .flatMap(course -> service.listById(course.getId())
                        .flatMap(c -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .body(BodyInserters.fromValue("Course already exist")))
                        .switchIfEmpty(service.register(course)
                                .flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
                                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                                        .body(fromValue(p))))
                );
    }

    public Mono<ServerResponse> modify(ServerRequest req) {
        Mono<Course> courseMono = req.bodyToMono(Course.class);

        return courseMono
                .flatMap(this.generalValidator::validate)
                .flatMap(course -> service.listById(course.getId())
                        .flatMap(courseFound ->
                                service.register(course)
                                    .flatMap(p -> ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_STREAM_JSON)
                                            .body(fromValue(p)))
                        )
                        ).switchIfEmpty(
                            ServerResponse
                                .notFound()
                                .build()
                        );

    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        Optional<String> id = req.queryParam("idCourse");

        return service.listById(id.get())
                .flatMap(p -> service.delete(p.getId())
                        .then(ServerResponse
                                .ok()
                                .build()
                        )
                )
                .switchIfEmpty(ServerResponse
                        .notFound()
                        .build());
    }

}
