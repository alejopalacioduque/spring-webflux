package com.reactor.academic.controller;

import com.reactor.academic.dto.CourseDTO;
import com.reactor.academic.exception.StudentException;
import com.reactor.academic.mapper.CourseMapper;
import com.reactor.academic.service.ICourseService;
import com.reactor.academic.exception.CourseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/listById")
    public Mono<ResponseEntity<CourseDTO>> listById(@Valid @RequestParam(name = "idCourse") String idCourse){
       return service.listById(idCourse)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(courseMapper.toDTO(p))
                )
               .defaultIfEmpty(ResponseEntity.notFound().build())
               .onErrorMap(error -> new CourseException("Error to list the course with ID:" + idCourse));
    }

    @PostMapping
    public Mono<ResponseEntity<CourseDTO>> register(@Valid @RequestBody CourseDTO courseDTO, final ServerHttpRequest req){
        return service.listById(courseDTO.getId()).flatMap( course ->
                Mono.just(ResponseEntity.accepted()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(courseMapper.toDTO(course)))
                ).switchIfEmpty(
                    service.register(courseMapper.toEntity(courseDTO))
                    .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                            .contentType(MediaType.APPLICATION_STREAM_JSON)
                            .body(courseMapper.toDTO(p))
                    ).onErrorMap(error ->
                            new CourseException("Error to register the course with ID:" + courseDTO.getId())
                    )
                );
    }

    @PutMapping
    public Mono<ResponseEntity<CourseDTO>> modify(@Valid @RequestBody CourseDTO courseDTO){
        return service.listById(courseDTO.getId())
                    .switchIfEmpty(
                        Mono.error(new CourseException("Course with ID:" + courseDTO.getId() + " Not found"))
                ).flatMap( course ->
                        service.modify(courseMapper.toEntity(courseDTO))
                                .map(p -> ResponseEntity.ok()
                                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                                        .body(courseMapper.toDTO(p))
                                )
                                .defaultIfEmpty(ResponseEntity.notFound().build())
                                .onErrorMap(error ->
                                        new CourseException("Error to modify the course with ID:" + courseDTO.getId()))
                );
    }

    @DeleteMapping()
    public Mono<ResponseEntity<Void>> delete(@Valid @RequestParam(name = "idCourse") String idCourse){
        return service.listById(idCourse).
                flatMap(p ->
                service.delete(idCourse).
                        then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))

                ).defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error ->
                        new CourseException(String.format("Error to delete the course with ID: %s", idCourse))
                );
    }
}
