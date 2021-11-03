package com.example.studentmanagement.professor.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(Professor.COLLECTION)
public class Professor extends BaseEntity {
    public static final String COLLECTION = "Professor";
    public static final String TEACHES_COURSES = "teachesCourses";

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotNull
    @Positive
    private Long personalNumber;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "M|F")
    private String gender;
    private String birthdate;
    private ArrayList<TeachesCourse> teachesCourses;




}
