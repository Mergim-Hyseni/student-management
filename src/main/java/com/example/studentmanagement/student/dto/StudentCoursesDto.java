package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.course.entities.Course;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StudentCoursesDto {
    public static final String COURSES = "courses";

    private Course courses;

}
