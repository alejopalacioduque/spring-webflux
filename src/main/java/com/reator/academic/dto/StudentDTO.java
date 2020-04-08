package com.reator.academic.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class StudentDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String dni;

    @NotNull
    private double age;
}
