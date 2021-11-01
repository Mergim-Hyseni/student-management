package com.example.studentmanagement.course.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@Builder
public class CourseDto {

    @NotBlank
    private String name;
    private int semester;
    @Positive
    private int ects;
    @NotBlank
    private String professorId;
    @Pattern(regexp = "O|M")
    private String type;
}
