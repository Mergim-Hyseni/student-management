package com.example.studentmanagement.professor.controllers;

import com.example.studentmanagement.professor.entities.Professor;
import com.example.studentmanagement.professor.services.ProfessorService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/professor", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfessorController {
    private final ProfessorService service;

    @PostMapping("")
    public Mono<Professor> addProfessor(@Valid @RequestBody Professor professor){
        return service.addProfessor(professor);
    }

    @GetMapping("/{id}")
    public Mono<Professor> getProfessorById(@PathVariable String id){
        return service.getProfessorById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Professor> deleteProfessorById(@PathVariable String id){
        return service.deleteProfessorById(id);
    }

}
