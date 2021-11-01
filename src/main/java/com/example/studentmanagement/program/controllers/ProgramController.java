package com.example.studentmanagement.program.controllers;

import com.example.studentmanagement.course.entities.Course;
import com.example.studentmanagement.program.entities.Passability;
import com.example.studentmanagement.program.entities.Program;
import com.example.studentmanagement.program.services.ProgramService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/program", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProgramController {
    private final ProgramService service;

    @PostMapping("")
    public Mono<Program> addProgram(@RequestBody Program program){
        return service.addProgram(program);
    }

    @GetMapping("/{id}")
    public Mono<Program> getProgramById(@PathVariable String id){
        return service.getProgramById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Program> deleteProgramById(@PathVariable String id){
        return service.deleteProgramById(id);
    }

    @PutMapping("/addPassability/{programId}")
    public Mono<Program> addCourse(@PathVariable String programId, @RequestBody @Valid Passability passability){
        return service.addPassability(programId,passability);
    }



}
