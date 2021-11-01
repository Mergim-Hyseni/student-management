package com.example.studentmanagement.semester.controllers;

import com.example.studentmanagement.semester.entities.Semester;
import com.example.studentmanagement.semester.services.SemesterService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/semester", produces = MediaType.APPLICATION_JSON_VALUE)
public class SemesterController {
    private final SemesterService service;

    @PostMapping("")
    public Mono<Semester> addSemester(@RequestBody @Valid Semester semester){
        return service.addSemester(semester);
    }

    @GetMapping("/{id}")
    public Mono<Semester> getSemesterById(@PathVariable String id){
        return service.getSemesterById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Semester> deleteSemesterById(String id){
        return service.deleteSemesterById(id);
    }



}
