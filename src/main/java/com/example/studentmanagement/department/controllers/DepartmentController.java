package com.example.studentmanagement.department.controllers;


import com.example.studentmanagement.department.entities.Department;
import com.example.studentmanagement.department.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/department", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {
    private final DepartmentService service;

    @PostMapping("")
    public Mono<Department> addDepartment(@RequestBody @Valid Department department) {
        return service.addDepartment(department);
    }

    @GetMapping("/{id}")
    public Mono<Department> getDepartmentById(@PathVariable String id) {
        return service.getDepartmentById(id);
    }

    @GetMapping("")
    public Flux<Department> getAllDepartments() {
        return service.getAllDepartments();
    }

    @DeleteMapping("/{id}")
    public Mono<Department> deleteDepartmentById(@PathVariable String id) {
        return service.deleteDepartmentById(id);
    }


}
