package com.example.studentmanagement.university.services;

import com.example.studentmanagement.university.entities.University;
import com.example.studentmanagement.university.repositories.UniversityRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
@AllArgsConstructor
public class UniversityService {

    private final UniversityRepo repo;

    public Mono<University> addUniversity(University university){
        return repo.addUniversity(university);
    }

    public Mono<University> getUniversityById(String id){
        return repo.getUniversityById(id);
    }

    public Mono<University> deleteUniversityById(String id){
        return repo.deleteUniversityById(id);
    }

    public Mono<Boolean> checkIfExistUniversityById(String universityId){
        return repo.checkIfExistUniversityById(universityId);
    }

}
