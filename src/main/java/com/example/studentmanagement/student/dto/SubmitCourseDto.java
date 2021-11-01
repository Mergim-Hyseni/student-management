package com.example.studentmanagement.student.dto;

import com.example.studentmanagement.student.entities.SubmitedCourse;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SubmitCourseDto {

    @NotBlank
    private String idStudent;
    @NotBlank
    private String idCourse;
    @NotBlank
    private String idDeadline;

   public SubmitedCourse convertDtoToEntity() {
       return SubmitedCourse.builder()
               .idCourse(idCourse)
               .grade(null)
               .idDeadline(idDeadline)
               .status(null)
               .build();
   }
}
