package com.example.studentmanagement.course.services;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.course.repositories.CourseRepository;
import com.example.studentmanagement.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository repository;

    public Mono<Course> addCourse(Course course) {
        return repository.addCourse(course);
    }

    public Flux<Course> getCoursesByProgramIdAndSemester(String programId, int semesterNumber) {
        return repository.getCoursesByProgramIdAndSemester(programId, semesterNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("Course not found")));
    }

    public Mono<Course> getCourseById(String id) {
        return repository.getCourseById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Course not found")));
    }

    public Mono<Course> deleteCourseById(String id) {
        return repository.deleteCourseById(id);
    }


    }
