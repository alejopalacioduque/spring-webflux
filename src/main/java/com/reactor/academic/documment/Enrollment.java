package com.reactor.academic.documment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "enrollments")
@Data
@NoArgsConstructor
public class Enrollment {

    @Id
    private String id;

    private LocalDateTime dateEnrollment = LocalDateTime.now();

    @DBRef
    @NotNull
    private Student student;

    @DBRef
    @NotNull
    private List<Course> enrollmentList;

    @NotNull
    private boolean state;
}
