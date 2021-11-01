package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.semester.entities.Semester;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StudentSemesterDto {
    public static final String SEMESTERS = "semesters";

    private ArrayList<Semester> semesters;

}
