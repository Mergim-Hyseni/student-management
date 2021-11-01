package com.example.studentmanagement.department.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotBlank;

@Data
@Document(Department.COLLECTION)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseEntity {
    public static final String COLLECTION = "Department";
    public static final String PROGRAMS = "programs";
    public static final String FACULTYID = "facultyId";
    public static final String UNIVERSITYID = "universityId";

    @NotBlank
    private String name;
    @NotBlank(message = "facultyId must not be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String facultyId;
    @NotBlank(message = "universityId must not be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String universityId;



}
