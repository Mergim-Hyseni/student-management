package com.example.studentmanagement.course.controllers;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.course.services.CourseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/course", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {
    private final CourseService service;

    @PostMapping("")
    public Mono<Course> addCourse(@RequestBody @Valid Course course) {
        return service.addCourse(course);
    }

    @GetMapping("/{id}")
    public Mono<Course> getCourseById(@PathVariable String id) {
        return service.getCourseById(id);
    }

    @GetMapping("")
    public Flux<Course> getCoursesByProgramIdAndSemesterNumber(@RequestParam String programId, @RequestParam Integer semesterNumber) {
        return service.getCoursesByProgramIdAndSemester(programId, semesterNumber);
    }

    @DeleteMapping("/{id}")
    public Mono<Course> deleteCourseById(@PathVariable String id) {
        return service.deleteCourseById(id);
    }






}
