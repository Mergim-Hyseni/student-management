package com.example.studentmanagement.professor.controllers;

import com.example.studentmanagement.professor.entities.Professor;
import com.example.studentmanagement.professor.entities.TeachesCourse;
import com.example.studentmanagement.professor.services.ProfessorService;
import com.example.studentmanagement.student.dto.GetStudentDto;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/professor", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfessorController {
    private final ProfessorService service;

    @PostMapping("")
    public Mono<Professor> addProfessor(@Valid @RequestBody Professor professor) {
        return service.addProfessor(professor);
    }

    @GetMapping("/{id}")
    public Mono<Professor> getProfessorById(@PathVariable String id) {
        return service.getProfessorById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Professor> deleteProfessorById(@PathVariable String id) {
        return service.deleteProfessorById(id);
    }

    @PutMapping("/addTeachCourse/{professorId}")
    public Mono<Professor> addTeachesCourse(@PathVariable String professorId, @RequestBody TeachesCourse teachesCourse) {
        return service.addTeachesCourse(professorId, teachesCourse);
    }

    @GetMapping("/studentsThatHasEnrolledCourse")
    public Flux<GetStudentDto> getStudentsThatHasEnrolledCourse(@RequestParam String professorId,
                                                                @RequestParam String programId, @RequestParam String courseId) {

        return service.getStudentsThatHasEnrolledCourse(professorId, programId, courseId);
    }

    @GetMapping("/studentsThatHasSubmittedCourse")
    public Flux<GetStudentDto> getStudentsByProgramIdSubmittedCourseId(@RequestParam String professorId
            , @RequestParam String deadlineId, @RequestParam String programId, @RequestParam String courseId) {
        return service.getStudentsThatHasSubmittedCourseId(professorId, deadlineId, programId, courseId);
    }

    @PutMapping("/gradeTheStudent")
    public Flux<GetStudentDto> gradeTheStudent(@RequestParam String professorId, @RequestParam String studentId,
                                               @RequestParam String programId, @RequestParam String courseId,
                                               @RequestParam String deadlineId, @RequestParam Integer grade) {

        return service.gradeTheStudent(professorId, programId, studentId, courseId, deadlineId, grade);
    }


}
