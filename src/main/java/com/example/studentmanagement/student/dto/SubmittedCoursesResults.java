package com.example.studentmanagement.student.dto;

import lombok.Data;
import lombok.Getter;

import java.time.Instant;

@Data
public class SubmittedCoursesResults {
    public static final String COURSE_NAME = "courseName";
    public static final String GRADE = "grade";
    public static final String STATUS = "status";
    public static final String GRADEPLACEDDATE = "gradePlacedDate";


    private String courseName;
    private Integer grade;
    private String status;
    private Instant gradePlacedDate;

}
