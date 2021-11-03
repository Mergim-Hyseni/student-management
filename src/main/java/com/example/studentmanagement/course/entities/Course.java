package com.example.studentmanagement.course.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(Course.COLLECTION)
@AllArgsConstructor
@Builder
public class Course extends BaseEntity {
    public static final String COLLECTION = "Course";
    public static final String PROGRAM_ID = "programId";
    public static final String SEMESTER = "semester";
    public static final String NAME = "name";

    @NotBlank
    private String name;
    @NotBlank
    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String programId;
    @NotNull
    @Positive
    private Integer semester;
    @Positive
    @NotNull
    private Integer ects;
    @NotBlank
    @Field(targetType = FieldType.OBJECT_ID)
    private String professorId;
    @NotNull
    @Pattern(regexp = "O|M")
    private String type;

}
