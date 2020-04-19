package com.reactor.academic.handler;

import com.reactor.academic.documment.Course;
import com.reactor.academic.documment.Enrollment;
import com.reactor.academic.exception.CourseException;
import com.reactor.academic.service.ICourseService;
import com.reactor.academic.service.IEnrollmentService;
import com.reactor.academic.service.IStudentService;
import com.reactor.academic.validators.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class EnrollmentHandler {

    private final IEnrollmentService service;

    private final RequestValidator generalValidator;

    private IStudentService studentService;

    private ICourseService courseService;

    @Autowired
    public EnrollmentHandler(IEnrollmentService service,
                             RequestValidator generalValidator,
                             IStudentService studentService,
                             ICourseService courseService) {
        this.service = service;
        this.generalValidator = generalValidator;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public Mono<ServerResponse> list(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(service.list(), Enrollment.class);
    }

    public Mono<ServerResponse> registrar(ServerRequest req){
        Mono<Enrollment> enrollmentMono = req.bodyToMono(Enrollment.class);

        return enrollmentMono
                .flatMap(this.generalValidator::validate)
                .flatMap(enrollment ->
                        studentService.listById(enrollment.getStudent().getId())
                        .flatMap(student ->
                                Flux.fromIterable(enrollment.getEnrollmentList()).flatMap(course ->
                                        courseService.listById(course.getId()).switchIfEmpty(
                                                Mono.error(new CourseException("Course with ID:" + course.getId() + " Not found"))
                                        )

                                ).then(
                                        service.register(enrollment).flatMap(p -> ServerResponse.created(
                                                URI.create(req.uri().toString().concat("/").concat(p.getId())))
                                                .contentType(MediaType.APPLICATION_STREAM_JSON)
                                                .body(fromValue(p))
                                        )
                                )
                        )
                        .switchIfEmpty( ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .body(BodyInserters.fromValue("Student with ID: " + enrollment.getStudent().getId() +" not exist"))
                        )
                );

    }

}
