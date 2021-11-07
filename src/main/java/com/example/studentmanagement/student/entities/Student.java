package com.example.studentmanagement.student.entities;

import com.example.studentmanagement.base.BaseEntity;
import com.example.studentmanagement.student.dto.GetStudentDto;
import com.example.studentmanagement.student.dto.StudentDto;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.ArrayList;


@Data
@EqualsAndHashCode(callSuper = true)
@Document(Student.COLLECTION)
@CompoundIndex(name = "personalNumber-idx", def = "{'personalNumber': 1}", unique = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {
    public static final String COLLECTION = "Student";
    public static final String SEMESTERS = "idSemesters";
    public static final String COURSES = "idCourses";
    public static final String DEADLINES = "idDeadlines";
    public static final String PROGRAM_ID = "programId";
    public static final String SUBMITTED_COURSES = "submittedCourses";
    public static final String SUBMITTED_COURSES_GRADE_PLACED_DATE = "submittedCourses." + SubmitedCourse.GRADE_PLACED_DATE;
    public static final String SUBMITTED_COURSES_IDCOURSE = "submittedCourses.idCourse";
    public static final String ECTS = "ects";
    public static final String SUBMITTED_COURSES_ECTS = "submittedCourses.idCourse.ects";
    public static final String SUBMITTED_COURSES_DEADLINE_ID = "submittedCourses." + SubmitedCourse.DEADLINE_ID ;
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email";

    @Indexed(direction = IndexDirection.DESCENDING)
    private String programId;
    private String departmentId;
    private String facultyId;
    private String universityId;
    private String name;
    private String surname;
    private Long personalNumber;
    private String birthdate;
    private String gender;
    private String email;
    private String password;
    private String telNumber;
    private String address;
    @Field(targetType = FieldType.OBJECT_ID)
    private ArrayList<String> idSemesters;
    @Field(targetType = FieldType.OBJECT_ID)
    private ArrayList<String>  idCourses;
    @Field(targetType = FieldType.OBJECT_ID)
    private ArrayList<String>  idDeadlines;
    private ArrayList<SubmitedCourse> submittedCourses;
    private Double average;
    private Integer ects;

    public StudentDto convertStudentToStudentDto() {
       StudentDto studentDto = new StudentDto();
       studentDto.setId(id);
       studentDto.setName(name);
       studentDto.setPersonalNumber(personalNumber);
       studentDto.setBirthdate(birthdate);
       studentDto.setGender(gender);
       studentDto.setTelNumber(telNumber);
       studentDto.setAddress(address);
       studentDto.setCreatedAt(createdAt);

       return studentDto;
    }

    public GetStudentDto convertStudentToGetStudentDto() {
        GetStudentDto getStudentDto = new GetStudentDto();
        getStudentDto.setId(id);
        getStudentDto.setName(name);
        getStudentDto.setSurname(surname);
        getStudentDto.setEmail(email);

        return getStudentDto;
    }

    public GetStudentDto convertStudentToGetStudentWithGradeDto() {
        GetStudentDto getStudentDto = new GetStudentDto();
        getStudentDto.setId(id);
        getStudentDto.setName(name);
        getStudentDto.setSurname(surname);
        getStudentDto.setEmail(email);
        return getStudentDto;
    }





}
