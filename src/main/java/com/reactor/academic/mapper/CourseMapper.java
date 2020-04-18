package com.reactor.academic.mapper;

import com.reactor.academic.documment.Course;
import com.reactor.academic.dto.CourseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CourseMapper {

    public Flux<CourseDTO> courseToDTO(Flux<Course> course) {
        return course.map(this::toDTO);
    }

    public Flux<Course> courseToEntity(Flux<CourseDTO> courseDTO) {
        return courseDTO.map(this::toEntity);
    }

    public Mono<CourseDTO> courseToDTO(Mono<Course> course) {
        return course.map(this::toDTO);
    }

    public Mono<Course> courseToEntity(Mono<CourseDTO> courseDTO) {
        return courseDTO.map(this::toEntity);
    }

    public CourseDTO toDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(course, courseDTO);
        return courseDTO;
    }

    public Course toEntity(CourseDTO courseDTO) {
        Course courseEntity = new Course();
        BeanUtils.copyProperties(courseDTO, courseEntity);
        return courseEntity;
    }
}
