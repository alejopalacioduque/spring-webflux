package com.reactor.academic.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CourseDTO {

    @NotEmpty(message = "The field Id is required")
    private String id;

    @NotEmpty(message = "The field name is required")
    private String name;

    @NotEmpty(message = "The field acronym is required")
    private String acronym;

    @NotNull(message = "The field state is required")
    private boolean state;
}
