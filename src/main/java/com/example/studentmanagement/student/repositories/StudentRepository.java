package com.example.studentmanagement.student.repositories;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.exceptions.NotFoundException;
import com.example.studentmanagement.semester.entities.Semester;
import com.example.studentmanagement.student.dto.*;
import com.example.studentmanagement.student.entities.Student;
import com.example.studentmanagement.student.entities.SubmitedCourse;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@AllArgsConstructor
public class StudentRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Student> addStudent(Student student) {
        return reactiveMongoTemplate.save(student);
    }

    public Mono<Student> getStudentById(String id) {
        return reactiveMongoTemplate.findById(id, Student.class)
                .switchIfEmpty(Mono.error(new NotFoundException("Student not found")));
    }

    public Mono<StudentEctsDto> getStudentEcts(String studentId){

        val c1 = Criteria.where(Student.ID).is(studentId);
        val c2 = Criteria.where(Student.SUBMITTED_COURSES_GRADE_PLACED_DATE).lt(Instant.now().minusSeconds(2 * 60));

        val groupOperation = group(Student.ID)
                .sum("course.ects").as("ects")
                .first(Student.PROGRAM_ID).as("programId")

                ;

        val aggregation = newAggregation(
                match(c1),
                unwind(Student.SUBMITTED_COURSES),
                match(c2),
                lookup(Course.COLLECTION,Student.SUBMITTED_COURSES_IDCOURSE, Course.ID, "course"),
                unwind("course")
                ,groupOperation
        );
        return reactiveMongoTemplate.aggregate(aggregation,Student.class,StudentEctsDto.class)
                .reduce((studentEctsDto, studentEctsDto2) -> studentEctsDto);
    }

    public Mono<Student> deleteStudentById(String id) {
        val criteria = Criteria.where(Student.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query, Student.class);
    }

    public Mono<Student> addSemester(String studentId, String semesterId) {
        val criteria = Criteria.where(Student.ID).is(studentId);
        val query = Query.query(criteria);
        val update = new Update().addToSet(Student.SEMESTERS).value(semesterId);
        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .then(getStudentById(studentId));
    }

    public Mono<Student> addCourse(String studentId, String courseId) {
        val criteria = Criteria.where(Student.ID).is(studentId);
        val query = Query.query(criteria);
        val update = new Update().addToSet(Student.COURSES).value(courseId);
        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .then(getStudentById(studentId));
    }

    public Mono<Void> addDeadline(String studentId, String deadlineId) {
        val criteria = Criteria.where(Student.ID).is(studentId);
        val query = Query.query(criteria);
        val update = new Update().addToSet(Student.DEADLINES).value(deadlineId);
        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .then(Mono.empty());
    }

    public Mono<StudentSemesterDto> getStudentSemesters(String studentId) {
        val aggregate = newAggregation(
                match(Criteria.where(Student.ID).is(studentId)),
                project(Student.SEMESTERS),
                lookup(Semester.COLLECTION, Student.SEMESTERS, Semester.ID, StudentSemesterDto.SEMESTERS)
        );
        return reactiveMongoTemplate.aggregate(aggregate, Student.class, StudentSemesterDto.class)
                .reduce((studentSemesterSDto, studentSemesterSDto2) -> studentSemesterSDto);
    }

    public Mono<Boolean> checkIfStudentContainsSemester(String studentId, int semesterNumber) {

        return getStudentSemesters(studentId)
                .<Boolean>handle((studentSemesterSDto, synchronousSink) -> {
                    val semesters = studentSemesterSDto.getSemesters();
                    semesters.forEach(semester -> {
                        if (semester.getNumber() == semesterNumber) synchronousSink.next(true);
                    });
                }).switchIfEmpty(Mono.just(false));

    }

    public Flux<Course> deleteRegistredCourse(String studentId, String courseId, int courseSemester) {
        val criteria = Criteria.where(Student.ID).is(studentId);
        val query = Query.query(criteria);
        val update = new Update().pull(Student.COURSES, courseId);

        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .flatMapMany(updateResult ->
                        updateResult.getModifiedCount() == 1 ? getRegistredCourses(studentId, courseSemester) : Mono.empty());
    }

    public Flux<Course> getRegistredCourses(String studentId, int semester) {
        val aggregation = newAggregation(
                match(Criteria.where(Student.ID).is(studentId)),
                lookup(Course.COLLECTION, Student.COURSES, Course.ID, StudentCoursesDto.COURSES),
                project(StudentCoursesDto.COURSES),
                match(Criteria.where(StudentCoursesDto.COURSES + "." + Course.SEMESTER).is(semester)),
                unwind(StudentCoursesDto.COURSES)
        );
        return reactiveMongoTemplate.aggregate(aggregation, Student.class, StudentCoursesDto.class)
                .map(studentCoursesDto -> studentCoursesDto.getCourses());
    }

    public Mono<List<SubmitedCourse>> getSubmittedCoursesByDeadline(String studentId, String deadlineId) {
        val criteria = Criteria.where(Student.ID).is(studentId)
                .and(Student.SUBMITTED_COURSES_DEADLINE_ID).is(deadlineId);

        val aggregation = newAggregation(
                match(criteria),
                project(Student.SUBMITTED_COURSES)
        );

        return reactiveMongoTemplate.aggregate(aggregation,Student.class, GetSubmittedCoursesDto.class)
                .reduce((getSubmittedCoursesDto, getSubmittedCoursesDto2) -> getSubmittedCoursesDto)
                  .map(getSubmittedCoursesDto -> getSubmittedCoursesDto.getSubmittedCourses())
                ;
    }

    public Mono<List<SubmitedCourse>> submitCourse(String studentId, SubmitedCourse submitedCourse) {
        val criteria = Criteria.where(Student.ID).is(studentId);
        val query = Query.query(criteria);
        val update = new Update().addToSet(Student.SUBMITTED_COURSES).value(submitedCourse);

        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .then(getSubmittedCoursesByDeadline(studentId, submitedCourse.getIdDeadline()));
    }

    public Mono<Boolean> checkIfStudentHasRegistredCourse(String studentId, String courseId) {
        val criteria1 = Criteria.where(Student.ID).is(studentId);
        val criteria2 = Criteria.where(Student.COURSES).in(courseId);
        val criteria = new Criteria().andOperator(criteria1, criteria2);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.exists(query, Student.class);
    }




}
