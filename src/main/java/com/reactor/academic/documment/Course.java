package com.reactor.academic.documment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @NotEmpty
    @Id
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String acronym;

    @NotNull
    private boolean state;
}
