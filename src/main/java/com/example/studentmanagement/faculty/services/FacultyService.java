package com.example.studentmanagement.faculty.services;

import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.faculty.entities.Faculty;
import com.example.studentmanagement.faculty.repositories.FacultyRepository;
import com.example.studentmanagement.university.services.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FacultyService {
    private final FacultyRepository repository;
    private final UniversityService universityService;

    public Mono<Faculty> addFaculty(Faculty faculty){
        return universityService.checkIfExistUniversityById(faculty.getUniversityId())
                .<Faculty>handle((exist, synchronousSink) -> {
                    if (exist) synchronousSink.next(faculty);
                    else synchronousSink.error(new BadRequestException("University of this Department doesn't exist"));
                }).flatMap(repository::addFaculty);
    }

    public Mono<Faculty> getFacultyById(String id){
        return repository.getFacultyById(id);
    }

    public Flux<Faculty> getAllFaculties(){
        return repository.getAllFaculties();
    }

    public Mono<Faculty> deleteFacultyById(String id){
        return repository.deleteFacultyById(id);
    }

    public Mono<Boolean> checkIfExistFacultyById(String facultyId){
        return repository.checkIfExistFacultyById(facultyId);
    }
}
