package com.example.studentmanagement.faculty.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(Faculty.COLLECTION)
@NoArgsConstructor
@AllArgsConstructor
public class Faculty extends BaseEntity {
    public static final String COLLECTION = "Faculty";
    public static final String DEPARTMENTS = "departments";
    public static final String UNIVERSITYID = "universityId";

    @NotBlank
    private String name;
    @NotBlank(message = "universityId field must not be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String universityId;
    @NotBlank
    private String address;

}
