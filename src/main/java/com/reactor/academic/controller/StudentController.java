package com.reactor.academic.controller;

import com.reactor.academic.dto.StudentDTO;
import com.reactor.academic.mapper.StudentMapper;
import com.reactor.academic.service.IStudentService;
import com.reactor.academic.exception.CourseException;
import com.reactor.academic.exception.StudentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        Flux<StudentDTO> fx = studentMapper.studentToDTO(service.list()).sort(
                Comparator.comparing(StudentDTO::getAge).reversed());

        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(fx));
    }

    @GetMapping("/listById")
    public Mono<ResponseEntity<StudentDTO>> listById(@Valid @RequestParam(name = "idStudent") String idStudent){
        return service.listById(idStudent)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(studentMapper.toDTO(p))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new StudentException("Error to list the student with ID:" + idStudent));
    }

    @PostMapping
    public Mono<ResponseEntity<StudentDTO>> register(@Valid @RequestBody StudentDTO studentDTO, final ServerHttpRequest req){
        return service.listById(studentDTO.getId()).flatMap( student ->
                Mono.just(ResponseEntity.accepted()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(studentMapper.toDTO(student)))
        ).switchIfEmpty(
                service.register(studentMapper.toEntity(studentDTO))
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(studentMapper.toDTO(p))
                ).onErrorMap(error ->
                        new StudentException("Error to register the student with Id:" + studentDTO.getId()))
        );
    }

    @PutMapping
    public Mono<ResponseEntity<StudentDTO>> modify(@Valid @RequestBody StudentDTO studentDTO){
        return service.listById(studentDTO.getId())
                .switchIfEmpty(
                    Mono.error(new StudentException("Student with ID:" + studentDTO.getId() + " Not found"))
                ).flatMap( student ->
                        service.modify(studentMapper.toEntity(studentDTO))
                                .map(p -> ResponseEntity.ok()
                                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                                        .body(studentMapper.toDTO(p))
                                )
                        .defaultIfEmpty(ResponseEntity.notFound().build())
                        .onErrorMap(error ->
                                new StudentException("Error to modify the student with ID:" + studentDTO.getId()))

                );
    }

    @DeleteMapping()
    public Mono<ResponseEntity<Void>> delete(@Valid @RequestParam(name = "idStudent") String idStudent){
        return service.listById(idStudent)
                .flatMap(est ->
                        service.delete(idStudent)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error ->
                        new StudentException(String.format("Error al eliminar el estudiante con Id: %s", idStudent))
                );
    }

    @GetMapping("/sortedByAgeParallel")
    public Mono<ResponseEntity<Flux<StudentDTO>>> listSortedByAgeParallel(){
        Flux<StudentDTO> fx = studentMapper.studentToDTO(service.list())
                .parallel()
                .runOn(Schedulers.elastic())
                .ordered(Comparator.comparing(StudentDTO::getAge).reversed());

        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(fx));
    }

}
