package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.student.entities.Student;
import com.example.studentmanagement.student.entities.SubmitedCourse;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GetSubmittedCoursesDto {
    public static final String SUBMITTED_COURSES = "submittedCourses";

    private ArrayList<SubmitedCourse> submittedCourses;

}
