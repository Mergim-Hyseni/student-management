package com.example.studentmanagement.university.controllers;

import com.example.studentmanagement.university.entities.University;
import com.example.studentmanagement.university.services.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/university", produces = MediaType.APPLICATION_JSON_VALUE)
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping("")
    public Mono<University> addUniversity(@Valid @RequestBody University university) {
        return universityService.addUniversity(university);
    }

    @GetMapping("/{id}")
    public Mono<University> getUniversityById(@PathVariable String id) {
        return universityService.getUniversityById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<University> deleteUniversityById(@PathVariable String id) {
        return universityService.deleteUniversityById(id);
    }


}
