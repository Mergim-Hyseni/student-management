package com.example.studentmanagement.professor.entities;

import com.example.studentmanagement.Base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeachesCourse extends BaseEntity {

    private String universityId;
    private String facultyId;
    private String departmentId;
    private String programId;
    private String courseId;



}
