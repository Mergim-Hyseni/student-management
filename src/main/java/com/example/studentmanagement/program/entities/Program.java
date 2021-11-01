package com.example.studentmanagement.program.entities;

import com.example.studentmanagement.Base.BaseEntity;
import com.example.studentmanagement.course.entities.Course;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.*;

@Data
@Document(Program.COLLECTION)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Program extends BaseEntity {
    public static final String COLLECTION = "Program";
    public static final String PASSABILITIES = "passabilities";
    public static final String PASSABILITIES_SEMESTER = "passabilities.semester";
    public static final String PASSABILITIES_ECTS = "passabilities.ects";
    public static final String LEVEL = "level";


    @NotBlank(message = "Program name must not be null")
    private String name;
    @NotBlank(message = "Department must not be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String departmentId;
    @NotBlank(message = "Faculty must not be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String facultyId;
    @NotBlank(message = "University must not be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String universityId;
    @Pattern(regexp = "Bachelor|Master|Phd")
    private String level;
    @NotNull
    @Positive(message = "Number of semesters must be positive number")
    private Integer numberOfSemesters;
    private Passability[] passabilities;



}
