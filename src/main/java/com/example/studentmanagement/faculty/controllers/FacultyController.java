package com.example.studentmanagement.faculty.controllers;

import com.example.studentmanagement.faculty.entities.Faculty;
import com.example.studentmanagement.faculty.services.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/faculty", produces = MediaType.APPLICATION_JSON_VALUE)
public class FacultyController {
    private final FacultyService service;

    @PostMapping("")
    public Mono<Faculty> addFaculty(@Valid @RequestBody Faculty faculty){
        return service.addFaculty(faculty);
    }

    @GetMapping("/{id}")
    public Mono<Faculty> getFacultyById(@PathVariable String id){
        return service.getFacultyById(id);
    }

    @GetMapping("")
    public Flux<Faculty> getAllFaculties(){
        return service.getAllFaculties();
    }

    @DeleteMapping("/{id}")
    public Mono<Faculty> deleteFacultyById(@PathVariable String id){
        return service.deleteFacultyById(id);
    }




}
