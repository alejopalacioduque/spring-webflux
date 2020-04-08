package com.reator.academic.mapper;

import com.reator.academic.documment.Course;
import com.reator.academic.documment.Enrollment;
import com.reator.academic.documment.Student;
import com.reator.academic.dto.CourseDTO;
import com.reator.academic.dto.EnrollmentDTO;
import com.reator.academic.dto.StudentDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EnrolledMapper {

    public Flux<EnrollmentDTO> enrollmentToDTO(Flux<Enrollment> enrollment) {
        return enrollment.map(this::toDTO);
    }

    public Flux<Enrollment> enrollmentToEntity(Flux<EnrollmentDTO> enrollmentDTO) {
        return enrollmentDTO.map(this::toEntity);
    }

    public Mono<EnrollmentDTO> enrollmentToDTO(Mono<Enrollment> enrollment) {
        return enrollment.map(this::toDTO);
    }

    public Mono<Enrollment> enrollmentToEntity(Mono<EnrollmentDTO> enrollmentDTO) {
        return enrollmentDTO.map(this::toEntity);
    }

    public EnrollmentDTO toDTO(Enrollment enrollment) {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        BeanUtils.copyProperties(enrollment, enrollmentDTO);
        return enrollmentDTO;
    }

    public Enrollment toEntity(EnrollmentDTO enrollmentDTO) {
        Enrollment enrollmentEntity = new Enrollment();
        BeanUtils.copyProperties(enrollmentDTO, enrollmentEntity);
        return enrollmentEntity;
    }
}
