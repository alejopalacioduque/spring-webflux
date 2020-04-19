package com.reactor.academic.handler;

import com.reactor.academic.documment.Student;
import com.reactor.academic.service.IStudentService;
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
import java.util.Comparator;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class StudentHandler {
    private final IStudentService service;

    private final RequestValidator generalValidator;

    @Autowired
    public StudentHandler(IStudentService service,
                             RequestValidator generalValidator) {
        this.service = service;
        this.generalValidator = generalValidator;
    }

    public Mono<ServerResponse> list(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(service.list().sort(Comparator.comparing(Student::getAge).reversed()), Student.class);
    }

    public Mono<ServerResponse> listParallel(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(service.list().parallel().sorted(Comparator.comparing(Student::getAge).reversed()), Student.class);
    }

    public Mono<ServerResponse> listById(ServerRequest req) {
        Optional<String> dni = req.queryParam("idStudent");

        return service.listById(dni.get())
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
        Mono<Student> studentMono = req.bodyToMono(Student.class);

        return studentMono
                .flatMap(this.generalValidator::validate)
                .flatMap(student -> service.listById(student.getDni())
                        .flatMap(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .body(BodyInserters.fromValue("Student already exist")))
                        .switchIfEmpty(service.register(student)
                                .flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
                                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                                        .body(fromValue(p))))
                );
    }

    public Mono<ServerResponse> modify(ServerRequest req) {
        Mono<Student> studentMono = req.bodyToMono(Student.class);

        return studentMono
                .flatMap(this.generalValidator::validate)
                .flatMap(student -> service.listById(student.getId())
                        .flatMap(studentFound ->
                            service.modify(student)
                                    .flatMap(p -> ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_STREAM_JSON)
                                            .body(fromValue(p)))
                        ))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        Optional<String> dni = req.queryParam("idStudent");

        return service.listById(dni.get())
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
