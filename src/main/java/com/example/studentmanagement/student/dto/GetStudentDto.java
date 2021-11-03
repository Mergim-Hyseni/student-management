package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.student.entities.SubmitedCourse;
import lombok.Data;

@Data
public class GetStudentDto {

    private String id;
    private String name;
    private String surname;
    private String email;
    private Integer grade;
    private SubmitedCourse submittedCourses;

}
