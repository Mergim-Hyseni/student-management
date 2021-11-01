package com.example.studentmanagement.student.services;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.course.services.CourseService;
import com.example.studentmanagement.deadline.services.DeadlineService;
import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.exceptions.NotFoundException;
import com.example.studentmanagement.program.services.ProgramService;
import com.example.studentmanagement.semester.services.SemesterService;
import com.example.studentmanagement.student.dto.*;
import com.example.studentmanagement.student.entities.Student;
import com.example.studentmanagement.student.entities.SubmitedCourse;
import com.example.studentmanagement.student.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;
    private final SemesterService semesterService;
    private final ProgramService programService;
    private final CourseService courseService;
    private final DeadlineService deadlineService;

    public Mono<Student> addStudent(AddStudentDto addStudentDto) {
        return repository.addStudent(addStudentDto.convertAddStudentDtoToStudent());
    }

    public Mono<Student> getStudentById(String id) {
        return repository.getStudentById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("StudentId does not exist")));
    }

    public Mono<Student> deleteStudentById(String id) {
        return repository.deleteStudentById(id);
    }

    public Mono<Student> addSemester(String studentId, String semesterId) {
        return Mono.zip(getStudentEcts(studentId), semesterService.getSemesterById(semesterId))
                .flatMap(objects -> {
                    val student = objects.getT1();
                    val semester = objects.getT2();
                    if (semester.getNumber() % 2 == 0 || semester.getNumber() == 1) {
                        return Mono.just(true);
                    } else
                        return programService.checkForPassability(student.getProgramId(), semester.getLevel(), semester.getNumber(), student.getEcts());
                }).flatMap(isTrue -> {
                    if (isTrue) return repository.addSemester(studentId, semesterId);
                    else return Mono.empty();
                }).switchIfEmpty(Mono.error(new BadRequestException("The student has not enough ects")));
    }

    public Mono<Student> addCourse(String studentId, String courseId) {
        return courseService.getCourseById(courseId)
                .flatMap(course -> repository.checkIfStudentContainsSemester(studentId, course.getSemester()))
                .flatMap(containsSemester -> containsSemester ? repository.addCourse(studentId, courseId) :
                        Mono.error(new BadRequestException("You can't register this course because you have not " +
                                "registred semester that this course belongs"))
                );
    }

    public Flux<Course> deleteRegistredCourse(String studentId, String courseId, int courseSemester) {
        return repository.deleteRegistredCourse(studentId, courseId, courseSemester);
    }

    public Flux<Course> getRegistredCourses(String studentId, int semester) {
        return repository.getRegistredCourses(studentId, semester);
    }

    public Flux<Course> getCoursesForStudentToRegister(String studentId, int semester) {
        return repository.getStudentById(studentId)
                .map(student -> student.getProgramId())
                .flatMapMany(programId -> courseService.getCoursesByProgramIdAndSemester(programId, semester));
    }

    public Mono<StudentSemesterDto> getStudentSemesters(String studentId) {
        return repository.getStudentSemesters(studentId);
    }

    public Mono<List<SubmitedCourse>> submitCourse(SubmitCourseDto submitCourseDto) {
        return repository.checkIfStudentHasRegistredCourse(submitCourseDto.getIdStudent(),submitCourseDto.getIdCourse())
                .flatMap(hasRegistred -> hasRegistred ? deadlineService.checkIfDeadlineIsAvailable(submitCourseDto.getIdDeadline())
                                                      : Mono.error(new BadRequestException("You must register course first")))
                .flatMap(isDeadLineAvailable -> isDeadLineAvailable
                        ? repository.submitCourse(submitCourseDto.getIdStudent(),submitCourseDto.convertDtoToEntity())
                        : Mono.error(new BadRequestException("There are not available deadlines")))
                .then(repository.getSubmittedCoursesByDeadline(submitCourseDto.getIdStudent(), submitCourseDto.getIdDeadline()));
    }

    public Mono<StudentEctsDto> getStudentEcts(String studentId) {
        return repository.getStudentEcts(studentId);
    }


    }
