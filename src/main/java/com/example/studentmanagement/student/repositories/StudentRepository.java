package com.example.studentmanagement.student.repositories;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.exceptions.NotFoundException;
import com.example.studentmanagement.semester.entities.Semester;
import com.example.studentmanagement.student.dto.*;
import com.example.studentmanagement.student.entities.Student;
import com.example.studentmanagement.student.entities.SubmitedCourse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
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

    public Mono<StudentEctsDto> getStudentEcts(String studentId) {

        val c1 = Criteria.where(Student.ID).is(studentId);
        val c2 = Criteria.where(Student.SUBMITTED_COURSES_GRADE_PLACED_DATE).lt(Instant.now().minusSeconds(2 * 60))
                .and(Student.SUBMITTED_COURSES + "." + SubmitedCourse.STATUS).is(SubmitedCourse.ACCEPTED_STATUS)
                .and(Student.SUBMITTED_COURSES + "." + SubmitedCourse.GRADE).gte(6).lte(10);

        val groupOperation = group(Student.ID)
                .sum("course.ects").as("ects")
                .first(Student.PROGRAM_ID).as(Student.PROGRAM_ID);

        val aggregation = newAggregation(
                match(c1),
                unwind(Student.SUBMITTED_COURSES),
                match(c2),
                lookup(Course.COLLECTION, Student.SUBMITTED_COURSES_IDCOURSE, Course.ID, "course"),
                unwind("course"),
                groupOperation
        );
        return reactiveMongoTemplate.aggregate(aggregation, Student.class, StudentEctsDto.class)
                .reduce((studentEctsDto, studentEctsDto2) -> studentEctsDto)
                .switchIfEmpty(getStudentById(studentId).map(student -> new StudentEctsDto(studentId, student.getProgramId()
                        , 0)))
                ;
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

        return reactiveMongoTemplate.aggregate(aggregation, Student.class, GetSubmittedCoursesDto.class)
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

    public Flux<GetStudentDto> getStudentsByProgramIdEnrolledCourseId(String programId, String courseId) {
        val c1 = Criteria.where(Student.PROGRAM_ID).is(programId);
        val c2 = Criteria.where(Student.COURSES).in(courseId);
        val c3 = Criteria.where(SubmitedCourse.COURSE_ID).is(courseId);
        val c4 = Criteria.where(SubmitedCourse.GRADE).gte(6).lte(10);
        val c5 = Criteria.where(SubmitedCourse.STATUS).is(SubmitedCourse.ACCEPTED_STATUS);
        val c6 = Criteria.where(SubmitedCourse.GRADE_PLACED_DATE).lt(Instant.now().minusSeconds(2 * 60));
        val c7 = new Criteria().andOperator(c3, c4, c5, c6);
        val c8 = Criteria.where(Student.SUBMITTED_COURSES).not().elemMatch(c7);
        val c = new Criteria().andOperator(c1, c2, c8);
        val query = Query.query(c);

        return reactiveMongoTemplate.find(query, Student.class)
                .map(student -> student.convertStudentToGetStudentDto());
    }

    public Flux<GetStudentDto> getStudentsThatHasSubmittedCourseId(String deadlineId, String programId, String courseId) {
        val c1 = Criteria.where(Student.PROGRAM_ID).is(programId);
        val c2 = Criteria.where(SubmitedCourse.COURSE_ID).is(courseId);
        val c3 = Criteria.where(SubmitedCourse.DEADLINE_ID).is(deadlineId);
        val c4 = new Criteria().andOperator(c2, c3);
        val c5 = Criteria.where(Student.SUBMITTED_COURSES).elemMatch(c4);
        val c = new Criteria().andOperator(c1, c5);

        val c6 = Criteria.where(Student.SUBMITTED_COURSES + "." + SubmitedCourse.COURSE_ID).is(new ObjectId(courseId));
        val c7 = Criteria.where(Student.SUBMITTED_COURSES + "." + SubmitedCourse.DEADLINE_ID).is(new ObjectId(deadlineId));
        val c8 = new Criteria().andOperator(c6, c7);

        val aggregation = newAggregation(
                match(c),
                project(Student.ID, Student.NAME, Student.SURNAME, Student.EMAIL, Student.SUBMITTED_COURSES),
                unwind(Student.SUBMITTED_COURSES),
                match(c8),
                project(Student.ID, Student.NAME, Student.SURNAME, Student.EMAIL)
                        .and(Student.SUBMITTED_COURSES + "." + SubmitedCourse.GRADE).as(SubmitedCourse.GRADE)
        );

        return reactiveMongoTemplate.aggregate(aggregation, Student.class, GetStudentDto.class);
    }


    public Flux<GetStudentDto> gradeTheStudent(String studentId, String programId, String courseId,
                                               String deadlineId, int grade) {

        val c1 = Criteria.where(Student.PROGRAM_ID).is(programId);
        val c2 = Criteria.where(Student.ID).is(studentId);
        val c3 = Criteria.where(SubmitedCourse.COURSE_ID).is(courseId)
                .and(SubmitedCourse.DEADLINE_ID).is(deadlineId);
        val c4 = new Criteria(Student.SUBMITTED_COURSES).elemMatch(c3);
        val c = new Criteria().andOperator(c1, c2, c4);
        val query = Query.query(c);

        val update = new Update().set(Student.SUBMITTED_COURSES + ".$." + SubmitedCourse.GRADE, grade)
                .currentDate(Student.SUBMITTED_COURSES + ".$." + SubmitedCourse.GRADE_PLACED_DATE)
                .set(Student.SUBMITTED_COURSES + ".$." + SubmitedCourse.STATUS, SubmitedCourse.ACCEPTED_STATUS);

        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .thenMany(getStudentsThatHasSubmittedCourseId(deadlineId, programId, courseId));
    }

    public Flux<SubmittedCoursesResults> getSubmittedCoursesResults(String studentId, String programId, String deadlineId) {
        val c1 = Criteria.where(Student.PROGRAM_ID).is(programId);
        val c2 = Criteria.where(Student.ID).is(studentId);
        val c3 = new Criteria().andOperator(c1, c2);

        val c4 = Criteria.where(Student.SUBMITTED_COURSES_DEADLINE_ID).is(new ObjectId(deadlineId));

        val aggregate = newAggregation(
                match(c3),
                project(Student.SUBMITTED_COURSES),
                unwind(Student.SUBMITTED_COURSES),
                match(c4),
                lookup(Course.COLLECTION, Student.SUBMITTED_COURSES_IDCOURSE, Course.ID, "course"),
                unwind("course"),
                project()
                        .and("course." + Course.NAME).as(SubmittedCoursesResults.COURSE_NAME)
                        .and(Student.SUBMITTED_COURSES + "." + SubmitedCourse.GRADE).as(SubmittedCoursesResults.GRADE)
                        .and(Student.SUBMITTED_COURSES + "." + SubmitedCourse.STATUS).as(SubmittedCoursesResults.STATUS)
                        .and(Student.SUBMITTED_COURSES + "." + SubmitedCourse.GRADE_PLACED_DATE).as(SubmittedCoursesResults.GRADEPLACEDDATE)

        );

        return reactiveMongoTemplate.aggregate(aggregate, Student.class, SubmittedCoursesResults.class);
    }

    public Flux<SubmittedCoursesResults> refuseGrade(String programId, String studentId, String courseId, String deadlineId) {
        val c1 = Criteria.where(Student.PROGRAM_ID).is(programId);
        val c2 = Criteria.where(Student.ID).is(studentId);
        val c3 = Criteria.where(Student.SUBMITTED_COURSES_IDCOURSE).is(courseId);
        val c4 = Criteria.where(Student.SUBMITTED_COURSES_DEADLINE_ID).is(deadlineId);
        val c5 = Criteria.where(Student.SUBMITTED_COURSES_GRADE_PLACED_DATE).gt(Instant.now().minusSeconds(2 * 60));

        val c6 = new Criteria().andOperator(c1, c2, c3, c4, c5);
        val query = Query.query(c6);

        val update = new Update().set(Student.SUBMITTED_COURSES + ".$." + SubmitedCourse.STATUS, SubmitedCourse.REFUSED_STATUS);

        return reactiveMongoTemplate.updateFirst(query, update, Student.class)
                .thenMany(getSubmittedCoursesResults(studentId, programId, deadlineId));
    }


}
