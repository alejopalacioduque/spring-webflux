package com.reator.academic.controller;

import com.reator.academic.documment.Student;
import com.reator.academic.exception.StudentException;
import com.reator.academic.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private IStudentService service;

    @Autowired
    public StudentController(IStudentService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Student>>> list(){
        Flux<Student> studentFlux = service.list();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(studentFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<Student>> register(@Valid @RequestBody Student student, final ServerHttpRequest req){
        return service.register(student)
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(p)
                ).onErrorMap(error -> new StudentException("Fail to register the student with Id:" + student.getId()));
    }
}
