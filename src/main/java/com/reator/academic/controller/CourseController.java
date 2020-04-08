package com.reator.academic.controller;

import com.reator.academic.dto.CourseDTO;
import com.reator.academic.exception.CourseException;
import com.reator.academic.mapper.CourseMapper;
import com.reator.academic.service.ICourseService;
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

    private CourseMapper courseMapper;

    @Autowired
    public CourseController(ICourseService service, CourseMapper courseMapper) {
        this.service = service;
        this.courseMapper = courseMapper;
    }

    @GetMapping()
    public Mono<ResponseEntity<Flux<CourseDTO>>> list(){
        Flux<CourseDTO> courseFlux = courseMapper.courseToDTO(service.list());

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(courseFlux));
    }

    @GetMapping("/{idCourse}")
    public Mono<ResponseEntity<CourseDTO>> listById(@Valid @PathVariable String idCourse){
       return service.listById(idCourse)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(courseMapper.toDTO(p))
                ).onErrorMap(error -> new CourseException("Fail to list the course with ID:" + idCourse));
    }

    @PostMapping
    public Mono<ResponseEntity<CourseDTO>> register(@Valid @RequestBody CourseDTO course, final ServerHttpRequest req){
        return service.register(courseMapper.toEntity(course))
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(courseMapper.toDTO(p))
                ).onErrorMap(error -> new CourseException("Fail to register the course with ID:" + course.getId()));
    }

    @PutMapping
    public Mono<ResponseEntity<CourseDTO>> modify(@Valid @RequestBody CourseDTO course){
        return service.modify(courseMapper.toEntity(course))
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(courseMapper.toDTO(p))
                ).onErrorMap(error -> new CourseException("Fail to modify the course with ID:" + course.getId()));
    }

    @DeleteMapping("/{idCourse}")
    public Mono<ResponseEntity<Void>> delete(@Valid @PathVariable String idCourse){
        return service.delete(idCourse)
                .map(p -> ResponseEntity.noContent().build());
    }
}
