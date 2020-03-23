package com.reator.academic.documment;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "enrollments")
@Data
public class Enrollment {

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateEnrollment;

    @DBRef
    private Student student;

    @DBRef
    private List<Course> enrollmentList;

    @NotNull
    private boolean state;
}
