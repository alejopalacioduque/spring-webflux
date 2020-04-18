package com.reactor.academic.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.*;

@Data
public class StudentDTO {

    @NotEmpty(message = "The field id is required")
    private String id;

    @NotEmpty(message = "The field first name is required")
    private String firstName;

    @NotEmpty(message = "The field last name is required")
    private String lastName;

    @NotEmpty(message = "The field dni is required")
    private String dni;

    @NotNull(message = "The field age is required")
    @Positive(message = "The field age will be a positive integer")
    @Min(value = 1, message = "The field age will be almost of 1 year")
    @Max(value = 100, message = "The field age will be less that 100 years")
    private double age;
}
