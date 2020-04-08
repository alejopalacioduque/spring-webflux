package com.reator.academic.dto;

import com.reator.academic.documment.Course;
import com.reator.academic.documment.Student;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnrollmentDTO {

    @NotNull
    private LocalDateTime dateEnrollment;

    @NotNull
    private Student student;

    @NotBlank
    private List<Course> enrollmentList;

    @NotNull
    private boolean state;
}
