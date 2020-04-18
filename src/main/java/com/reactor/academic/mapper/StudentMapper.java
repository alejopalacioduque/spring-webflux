package com.reactor.academic.mapper;

import com.reactor.academic.documment.Student;
import com.reactor.academic.dto.StudentDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StudentMapper {

    public Flux<StudentDTO> studentToDTO(Flux<Student> student) {
        return student.map(this::toDTO);
    }

    public Flux<Student> studentToEntity(Flux<StudentDTO> studentDTO) {
        return studentDTO.map(this::toEntity);
    }

    public Mono<StudentDTO> studentToDTO(Mono<Student> student) {
        return student.map(this::toDTO);
    }

    public Mono<Student> studentToEntity(Mono<StudentDTO> studentDTO) {
        return studentDTO.map(this::toEntity);
    }

    public StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        BeanUtils.copyProperties(student, studentDTO);
        return studentDTO;
    }

    public Student toEntity(StudentDTO studentDTO) {
        Student studentEntity = new Student();
        BeanUtils.copyProperties(studentDTO, studentEntity);
        return studentEntity;
    }

}
