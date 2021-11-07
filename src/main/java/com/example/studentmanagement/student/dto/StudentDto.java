package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.base.BaseEntity;
import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.deadline.entities.Deadline;
import com.example.studentmanagement.program.entities.Program;
import com.example.studentmanagement.semester.entities.Semester;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StudentDto extends BaseEntity {

    private Program programId;
    private String name;
    private String surname;
    private long personalNumber;
    private String birthdate;
    private String gender;
    private String email;
    private String password;
    private String telNumber;
    private String address;
    private ArrayList<Semester> semesters;
    private ArrayList<Course> courses;
    private ArrayList<Deadline> deadlines;
    private Double average;
    private Integer ects;



}
