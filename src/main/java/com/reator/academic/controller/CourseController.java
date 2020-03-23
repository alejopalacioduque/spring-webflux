package com.reator.academic.controller;

import com.reator.academic.documment.Course;
import com.reator.academic.documment.Student;
import com.reator.academic.exception.CourseException;
import com.reator.academic.service.ICourseService;
import com.reator.academic.service.IStudentService;
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
@RequestMapping("/api/courses")
public class CourseController {

    private ICourseService service;

    @Autowired
    public CourseController(ICourseService service) {
        this.service = service;
    }

    @GetMapping()
    public Mono<ResponseEntity<Flux<Course>>> list(){
        Flux<Course> courseFlux = service.list();

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(courseFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<Course>> register(@Valid @RequestBody Course course, final ServerHttpRequest req){
        return service.register(course)
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(p)
                ).onErrorMap(error -> new CourseException("Fail to register the course with ID:" + course.getId()));
    }
}
