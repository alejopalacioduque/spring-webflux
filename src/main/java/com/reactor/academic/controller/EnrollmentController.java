package com.reactor.academic.controller;

import com.reactor.academic.documment.Student;
import com.reactor.academic.dto.EnrollmentDTO;
import com.reactor.academic.exception.CourseException;
import com.reactor.academic.exception.StudentException;
import com.reactor.academic.mapper.EnrolledMapper;
import com.reactor.academic.service.ICourseService;
import com.reactor.academic.service.IEnrollmentService;
import com.reactor.academic.exception.EnrollmentException;
import com.reactor.academic.service.IStudentService;
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

    private IStudentService studentService;

    private ICourseService courseService;

    @Autowired
    public EnrollmentController(IEnrollmentService enrollmentService, EnrolledMapper enrolledMapper,
                                IStudentService studentService, ICourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.enrolledMapper = enrolledMapper;
        this.studentService = studentService;
        this.courseService = courseService;
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

        String idStudent = enrollment.getStudent().getId();
        return studentService.listById(idStudent).switchIfEmpty(
                Mono.error(new StudentException("Student with ID:" + idStudent + " Not found"))
        ).flatMap(student ->
                Flux.fromIterable(enrollment.getEnrollmentList()).flatMap( course ->
                        courseService.listById(course.getId()).switchIfEmpty(
                                Mono.error(new CourseException("Course with ID:" + course.getId() + " Not found"))
                        )

                ).then(
                        enrollmentService.register(enrolledMapper.toEntity(enrollment))
                                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/")))
                                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                                        .body(enrolledMapper.toDTO(p))
                                ).onErrorMap(error -> new EnrollmentException("Error to register the enrollment"))
                )
        );
    }

}
