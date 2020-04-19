package com.reactor.academic.dto;

import com.reactor.academic.documment.Course;
import com.reactor.academic.documment.Student;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnrollmentDTO {

    @NotNull(message = "The field student is required")
    private Student student;

    @NotNull(message = "The field enrollment list is required")
    private List<Course> enrollmentList;

    @NotNull(message = "The field state is required")
    private boolean state;
}
