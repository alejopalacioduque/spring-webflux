package com.reator.academic.controller;

import com.reator.academic.documment.Student;
import com.reator.academic.dto.CourseDTO;
import com.reator.academic.dto.StudentDTO;
import com.reator.academic.exception.CourseException;
import com.reator.academic.exception.StudentException;
import com.reator.academic.mapper.StudentMapper;
import com.reator.academic.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private IStudentService service;

    private StudentMapper studentMapper;

    @Autowired
    public StudentController(IStudentService service, StudentMapper studentMapper) {
        this.service = service;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<StudentDTO>>> list(){
        Flux<StudentDTO> studentFlux = studentMapper.studentToDTO(service.list());

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(studentFlux));
    }

    @GetMapping("/{idStudent}")
    public Mono<ResponseEntity<StudentDTO>> listById(@Valid @PathVariable String idStudent){
        return service.listById(idStudent)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(studentMapper.toDTO(p))
                ).onErrorMap(error -> new CourseException("Fail to list the student with ID:" + idStudent));
    }

    @PostMapping
    public Mono<ResponseEntity<StudentDTO>> register(@Valid @RequestBody StudentDTO studentDTO, final ServerHttpRequest req){
        return service.register(studentMapper.toEntity(studentDTO))
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(studentMapper.toDTO(p))
                ).onErrorMap(error -> new StudentException("Fail to register the student with Id:" + studentDTO.getId()));
    }

    @PutMapping
    public Mono<ResponseEntity<StudentDTO>> modify(@Valid @RequestBody StudentDTO studentDTO){
        return service.modify(studentMapper.toEntity(studentDTO))
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(studentMapper.toDTO(p))
                ).onErrorMap(error -> new CourseException("Fail to modify the student with ID:" + studentDTO.getId()));
    }

    @DeleteMapping("/{idStudent}")
    public Mono<ResponseEntity<Void>> delete(@Valid @PathVariable String idStudent){
        return service.delete(idStudent)
                .map(p -> ResponseEntity.noContent().build());
    }

    @GetMapping("/sortedByAgeParallel")
    public Mono<ResponseEntity<Flux<StudentDTO>>> listSortedByAgeParallel(){
        Flux<StudentDTO> fx = studentMapper.studentToDTO(service.list())
                .parallel()
                .runOn(Schedulers.elastic())
                .ordered((p1, p2) -> (int)p2.getAge() - (int)p1.getAge());

        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(fx));
    }

    @GetMapping("/sortedByAge")
    public Mono<ResponseEntity<Flux<StudentDTO>>> listSortedByAge(){
        Flux<StudentDTO> fx = studentMapper.studentToDTO(service.list()).sort(
                (p1, p2) -> (int)p2.getAge() - (int)p1.getAge());

        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(fx));
    }

}
