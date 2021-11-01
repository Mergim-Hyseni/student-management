package com.example.studentmanagement.professor.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("")
public class Professor extends BaseEntity {
    private final String collection = "Professor";

    private String name;
    private String surname;
    private long personalNumber;



}
