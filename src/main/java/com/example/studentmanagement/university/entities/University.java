package com.example.studentmanagement.university.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document(University.COLLECTION)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class University extends BaseEntity {
    public static final String COLLECTION = "University";
    public static final String FACULTIES = "faculties";

    @NotBlank
    private String name;
    @NotBlank
    private String state;
    @NotBlank
    private String city;




}
