package com.example.studentmanagement.professor.services;

import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.professor.entities.Professor;
import com.example.studentmanagement.professor.entities.TeachesCourse;
import com.example.studentmanagement.professor.repositories.ProfessorRepository;
import com.example.studentmanagement.student.dto.GetStudentDto;
import com.example.studentmanagement.student.services.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class ProfessorService {
    private final ProfessorRepository repository;
    private final StudentService studentService;


    public Mono<Professor> addProfessor(Professor professor) {
        return repository.addProfessor(professor);
    }

    public Mono<Professor> getProfessorById(String id) {
        return repository.getProfessorById(id);
    }

    public Mono<Professor> deleteProfessorById(String id) {
        return repository.deleteProfessorById(id);
    }

    public Mono<Professor> addTeachesCourse(String professorId, TeachesCourse teachesCourse) {
        return repository.addTeachesCourse(professorId, teachesCourse);
    }

    public Flux<GetStudentDto> getStudentsThatHasEnrolledCourse(String professorId, String programId, String courseId) {
        return repository.checkIfProfessorTeachCourse(professorId, courseId)
                .flatMapMany(contains -> contains ? studentService.getStudentsByProgramIdEnrolledCourseId(programId, courseId)
                        : Mono.error(new BadRequestException("You must have to registred teach Courses" +
                        " this course to view students that has enrolled"))
                );
    }

    public Flux<GetStudentDto> getStudentsThatHasSubmittedCourseId(String professorId, String deadlineId,
                                                                   String programId, String courseId) {
        return repository.checkIfProfessorTeachCourse(professorId, courseId)
                .flatMapMany(contains -> contains ? studentService.getStudentsThatHasSubmittedCourseId(deadlineId, programId, courseId)
                        : Mono.error(new BadRequestException("incorrect course"))
                );
    }

    public Flux<GetStudentDto> gradeTheStudent(String professorId, String programId, String studentId,
                                               String courseId, String deadlineId, int grade) {
        return repository.checkIfProfessorTeachCourse(professorId, courseId)
                .flatMapMany(contains -> contains ? studentService.gradeTheStudent(studentId, programId, courseId,
                        deadlineId,grade)
                        : Mono.error(new BadRequestException("incorrect course"))
                );
    }


}
