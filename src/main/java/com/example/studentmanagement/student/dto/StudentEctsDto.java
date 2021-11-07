package com.example.studentmanagement.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentEctsDto {
    private String id;
    private String programId;
    private Integer ects;
}
