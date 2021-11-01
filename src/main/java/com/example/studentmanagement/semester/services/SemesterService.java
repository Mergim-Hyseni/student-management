package com.example.studentmanagement.semester.services;

import com.example.studentmanagement.exceptions.BadRequestException;
import com.example.studentmanagement.semester.entities.Semester;
import com.example.studentmanagement.semester.repositories.SemesterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SemesterService {
    private final SemesterRepository repository;

    public Mono<Semester> addSemester(Semester semester){
        return repository.addSemester(semester);
    }

    public Mono<Semester> getSemesterById(String id){
        return repository.getSemesterById(id)
                .switchIfEmpty(Mono.error(new BadRequestException("SemesterId does not exist")));
    }

    public Mono<Semester> deleteSemesterById(String id) {
        return repository.deleteSemesterById(id);
    }


}
