package com.example.studentmanagement.deadline.controllers;

import com.example.studentmanagement.deadline.entities.Deadline;
import com.example.studentmanagement.deadline.services.DeadlineService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/deadline", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeadlineController {
    private final DeadlineService service;

    @PostMapping("")
    public Mono<Deadline> addDeadline(@RequestBody @Valid Deadline deadline){
        return service.addDeadline(deadline);
    }

    @GetMapping("/{id}")
    public Mono<Deadline> getDeadlineById(@PathVariable String id){
        return service.getDeadlineById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Deadline> deleteDeadlineById(@PathVariable String id){
        return service.deleteDeadlineById(id);
    }

}
