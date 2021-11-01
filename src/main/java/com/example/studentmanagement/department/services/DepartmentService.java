package com.example.studentmanagement.department.services;

import com.example.studentmanagement.department.entities.Department;
import com.example.studentmanagement.department.repositories.DepartmentRepository;
import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.faculty.services.FacultyService;
import com.example.studentmanagement.university.services.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repository;
    private final FacultyService facultyService;
    private final UniversityService universityService;

    public Mono<Department> addDepartment(Department department) {
        return facultyService.checkIfExistFacultyById(department.getFacultyId())
                .handle((facultyExist, synchronousSink) -> {
                    if (facultyExist) synchronousSink.complete();
                    else synchronousSink.error(new BadRequestException("Faculty of this Department doesn't exist"));
                }).then(universityService.checkIfExistUniversityById(department.getUniversityId()))
                .<Department>handle((exist, synchronousSink) -> {
                    if (exist) synchronousSink.next(department);
                    else synchronousSink.error(new BadRequestException("University of this Department doesn't exist"));
                })
                .flatMap(repository::addDepartment);
    }

    public Mono<Department> getDepartmentById(String id) {
        return repository.getDepartmentById(id);
    }

    public Flux<Department> getAllDepartments() {
        return repository.getAllDepartments();
    }

    public Mono<Department> deleteDepartmentById(String id) {
        return repository.deleteDepartmentById(id);
    }

    public Mono<Boolean> checkIfExistDepartmentWithId(String id) {
       return repository.checkIfExistDepartmentWithId(id);
    }

}
