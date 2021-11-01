package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.student.entities.Student;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class AddStudentDto {

    @NotBlank
    private String programId;
    @NotBlank
    private String departmentId;
    @NotBlank
    private String facultyId;
    @NotBlank
    private String universityId;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Positive
    private Long personalNumber;
    private String birthdate;
    @Pattern(regexp = "M|F")
    private String gender;
    @Email
    private String email;
    private String telNumber;
    @NotBlank
    private String address;

    public Student convertAddStudentDtoToStudent(){
        return Student.builder()
                .programId(programId)
                .departmentId(departmentId)
                .facultyId(facultyId)
                .universityId(universityId)
                .name(name)
                .surname(surname)
                .personalNumber(personalNumber)
                .birthdate(birthdate)
                .gender(gender)
                .email(email)
                .password(personalNumber.toString())
                .telNumber(telNumber)
                .address(address)
                .build();
    }


}
