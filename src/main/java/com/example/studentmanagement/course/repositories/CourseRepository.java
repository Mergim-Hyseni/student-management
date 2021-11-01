package com.example.studentmanagement.course.repositories;

import com.example.studentmanagement.course.entities.Course;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CourseRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Course> addCourse(Course course) {
        return reactiveMongoTemplate.save(course);
    }

    public Flux<Course> getCoursesByProgramIdAndSemester(String programId, int semester) {
        val criteria = Criteria.where(Course.PROGRAM_ID).is(programId)
                .and(Course.SEMESTER).is(semester);
        val query = Query.query(criteria);

        return reactiveMongoTemplate.find(query, Course.class);
    }

    public Mono<Course> getCourseById(String id) {
        return reactiveMongoTemplate.findById(id, Course.class);
    }

    public Mono<Course> deleteCourseById(String id) {
        val criteria = Criteria.where(Course.ID).is(id);
        val query = Query.query(criteria);
        return reactiveMongoTemplate.findAndRemove(query, Course.class);
    }


}
