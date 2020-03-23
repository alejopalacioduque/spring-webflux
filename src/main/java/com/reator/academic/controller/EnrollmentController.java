package com.reator.academic.controller;

import com.reator.academic.documment.Enrollment;
import com.reator.academic.documment.Student;
import com.reator.academic.exception.EnrollmentException;
import com.reator.academic.exception.StudentException;
import com.reator.academic.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private IEnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(IEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Enrollment>>> list(){
        Flux<Enrollment> studentFlux = enrollmentService.list();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(studentFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<Enrollment>> register(@Valid @RequestBody Enrollment enrollment, final ServerHttpRequest req){
        return enrollmentService.register(enrollment)
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/")))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(p)
                ).onErrorMap(error -> new EnrollmentException("Fail to register the enrollment"));
    }
}
