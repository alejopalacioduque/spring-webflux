package com.reator.academic.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CourseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String acronym;

    @NotNull
    private boolean state;
}
