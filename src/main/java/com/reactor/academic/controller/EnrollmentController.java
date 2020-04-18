package com.reactor.academic.controller;

import com.reactor.academic.dto.EnrollmentDTO;
import com.reactor.academic.mapper.EnrolledMapper;
import com.reactor.academic.service.IEnrollmentService;
import com.reactor.academic.exception.EnrollmentException;
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

    private EnrolledMapper enrolledMapper;

    @Autowired
    public EnrollmentController(IEnrollmentService enrollmentService, EnrolledMapper enrolledMapper) {
        this.enrollmentService = enrollmentService;
        this.enrolledMapper = enrolledMapper;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<EnrollmentDTO>>> list(){
        Flux<EnrollmentDTO> studentFlux = enrolledMapper.enrollmentToDTO(enrollmentService.list());

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(studentFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<EnrollmentDTO>> register(@Valid @RequestBody EnrollmentDTO enrollment, final ServerHttpRequest req){
        return enrollmentService.register(enrolledMapper.toEntity(enrollment))
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/")))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(enrolledMapper.toDTO(p))
                ).onErrorMap(error -> new EnrollmentException("Error to register the enrollment"));
    }

}
