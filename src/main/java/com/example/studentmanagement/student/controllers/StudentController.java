package com.example.studentmanagement.student.controllers;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.student.dto.*;
import com.example.studentmanagement.student.entities.Student;
import com.example.studentmanagement.student.entities.SubmitedCourse;
import com.example.studentmanagement.student.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {
    private final StudentService service;

    @PostMapping("")
    public Mono<Student> addStudent(@RequestBody @Valid AddStudentDto studentDto) {
        return service.addStudent(studentDto);
    }

    @GetMapping("/{id}")
    public Mono<Student> getStudentById(@PathVariable String id) {
        return service.getStudentById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Student> deleteStudentById(@PathVariable String id) {
        return service.deleteStudentById(id);
    }

    @PutMapping("/addSemester")
    public Mono<Student> addSemester(@RequestParam String studentId, @RequestParam String semesterId) {
        return service.addSemester(studentId, semesterId);
    }

    @PutMapping("/addCourse")
    public Mono<Student> addCourse(@RequestParam String studentId, @RequestParam String courseId) {
        return service.addCourse(studentId, courseId);
    }

    @DeleteMapping("/courses")
    public Flux<Course> deleteRegistredCourse(@RequestParam String studentId, @RequestParam String courseId,
                                              @RequestParam int courseSemester) {
        return service.deleteRegistredCourse(studentId, courseId, courseSemester);
    }

    @GetMapping("/courses")
    public Flux<Course> getRegistredCourses(@RequestParam String studentId, @RequestParam int semester) {
        return service.getRegistredCourses(studentId, semester);
    }

    @GetMapping("/coursesToRegister")
    public Flux<Course> getCoursesForStudentToRegister(@RequestParam String studentId, @RequestParam Integer semester) {
        return service.getCoursesForStudentToRegister(studentId, semester);
    }

    @GetMapping("/semesters")
    public Mono<StudentSemesterDto> getSemesters(@RequestParam String studentId) {
        return service.getStudentSemesters(studentId);
    }

    @PostMapping("/submitCourse")
    public Mono<List<SubmitedCourse>> submitCourse(@RequestBody @Valid SubmitCourseDto submitCourseDto) {
        return service.submitCourse(submitCourseDto);
    }

    @GetMapping("/ects")
    public Mono<StudentEctsDto> getStudentEcts(@RequestParam String studentId) {
        return service.getStudentEcts(studentId);
    }


}
