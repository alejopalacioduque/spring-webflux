package com.reator.academic.documment;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "courses")
@Data
public class Course {

    @Id
    private String id;

    @NotEmpty
    private String name;

    private String acronym;

    @NotNull
    private boolean state;
}
