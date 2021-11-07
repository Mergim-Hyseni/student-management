package com.example.studentmanagement.semester.entities;

import com.example.studentmanagement.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(Semester.COLLECTION)
@Builder
public class Semester extends BaseEntity {
    public static final String COLLECTION = "Semester";
    public static final String SEMESTER_NUMBER = "number";

    @Positive
    @NotNull
    private Integer number;
    @NotBlank
    @Pattern(regexp = "Bachelor|Master|Phd")
    private String level;
    @NotBlank
    private String description;
    @NotNull
    private Integer startYear;
    @NotNull
    private Integer endYear;


}
