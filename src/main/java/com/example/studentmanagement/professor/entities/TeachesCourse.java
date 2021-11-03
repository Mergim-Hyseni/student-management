package com.example.studentmanagement.professor.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotBlank;

@Data
public class TeachesCourse {
    public static final String COURSE_ID = "courseId";

    @Field(targetType = FieldType.OBJECT_ID)
    @NotBlank private String universityId;
    @Field(targetType = FieldType.OBJECT_ID)
    @NotBlank private String facultyId;
    @Field(targetType = FieldType.OBJECT_ID)
    @NotBlank private String departmentId;
    @Field(targetType = FieldType.OBJECT_ID)
    @NotBlank private String programId;
    @Field(targetType = FieldType.OBJECT_ID)
    @NotBlank private String courseId;



}
